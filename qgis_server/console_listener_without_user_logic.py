import threading
from http.server import BaseHTTPRequestHandler, HTTPServer
import json
import processing
from qgis.core import (
    QgsMessageLog, Qgis,
    QgsProject, QgsVectorLayer, QgsFeature
)
import psycopg2
from datetime import datetime, date, time

from qgis.PyQt.QtCore import QVariant, QDateTime, QDate, QTime

HOST = "127.0.0.1"
PORT = 8102
ALGO_ID = "project:Найкоротший маршрут_бордюри"


def convert_value(v):
    """Повна універсальна конвертація всіх QGIS/Qt типів у Python."""

    if v is None or isinstance(v, QVariant) or v == QVariant():
        return None

    if isinstance(v, QDateTime):
        return v.toPyDateTime()

    if isinstance(v, QDate):
        return v.toPyDate()

    if isinstance(v, QTime):
        return v.toPyTime()

    if hasattr(v, "toPyObject"):
        try:
            return v.toPyObject()
        except:
            pass

    return v


class QgisHandler(BaseHTTPRequestHandler):

    def _send_json(self, status, obj):
        self.send_response(status)
        self.send_header("Content-type", "application/json")
        self.end_headers()
        self.wfile.write(json.dumps(obj).encode())

    def do_POST(self):
        if self.path != "/run":
            self._send_json(404, {"error": "Not found"})
            return

        try:
            length = int(self.headers.get("Content-Length"))
            body = self.rfile.read(length).decode()
            data = json.loads(body)

            max_height = data.get("height")
            start_point = data.get("start")
            route_key = data.get("route_key")

            if max_height is None or start_point is None or route_key is None:
                self._send_json(400, {"error": "Missing required fields"})
                return

            alg_params = {
                "": max_height,
                " (2)": start_point,
                "1": "postgres://dbname='geodatabase' host=127.0.0.1 "
                     "port=5433 user='postgres' password='post_hack' sslmode=disable "
                     "key='\"id\"' srid=5564 type=Point table=\"geo_score_schema\".\"locations_view\" (coordinates)",

                "native:difference_1:Мережа": 'TEMPORARY_OUTPUT',
                "native:extractbyattribute_2:Мережа маршрутів до об\'єктів": 'TEMPORARY_OUTPUT',
                "native:extractbyexpression_1:Найкоротший маршрут": "TEMPORARY_OUTPUT",
                "native:fieldcalculator_1:Бар\'єр": "TEMPORARY_OUTPUT"
            }

            QgsMessageLog.logMessage("Running processing model...", "QGIS", Qgis.Info)
            results = processing.run(ALGO_ID, alg_params)

            route_layer: QgsVectorLayer = results["native:extractbyexpression_1:Найкоротший маршрут"]

            if route_layer is None or not route_layer.isValid():
                self._send_json(500, {"error": "Temporary route layer not produced"})
                return

            conn = psycopg2.connect(
                dbname="geodatabase",
                user="postgres",
                password="post_hack",
                host="127.0.0.1",
                port="5433"
            )
            cur = conn.cursor()

            fields = route_layer.fields()
            field_names = fields.names()

            for feat in route_layer.getFeatures():
                geom_wkb = feat.geometry().asWkb()
                raw_attrs = feat.attributes()
                attrs = {field_names[i]: convert_value(raw_attrs[i]) for i in range(len(field_names))}

                def get(name):
                    return attrs.get(name)

                cur.execute("""
                    INSERT INTO geo_score_schema.shortest_routes_history (
                        geom,
                        id, name, address,
                        created_at, created_by, description,
                        last_verified_at, last_verified_by,
                        organization_id, overall_accessibility_score,
                        rejection_reason, status, updated_at, updated_by,
                        location_type_id, image_service_id, start, "end",
                        cost, cost1, route_key
                    )
                    VALUES (
                        ST_SetSRID(%s::geometry, 5564),
                        %s, %s, %s,
                        %s, %s, %s,
                        %s, %s,
                        %s, %s,
                        %s, %s, %s, %s,
                        %s, %s, %s, %s,
                        %s, %s, %s
                    )
                """, (
                    psycopg2.Binary(geom_wkb),
                    get("id"),
                    get("name"),
                    get("address"),
                    get("created_at"),
                    get("created_by"),
                    get("description"),
                    get("last_verified_at"),
                    get("last_verified_by"),
                    get("organization_id"),
                    get("overall_accessibility_score"),
                    get("rejection_reason"),
                    get("status"),
                    get("updated_at"),
                    get("updated_by"),
                    get("location_type_id"),
                    get("image_service_id"),
                    get("start"),
                    get("end"),
                    get("cost"),
                    get("cost1"),
                    route_key
                ))

            conn.commit()
            cur.close()
            conn.close()

            self._send_json(200, {"status": "ok"})

        except Exception as e:
            self._send_json(500, {"error": str(e)})


def start_server():
    server = HTTPServer((HOST, PORT), QgisHandler)
    QgsMessageLog.logMessage(f"QGIS HTTP server started at {HOST}:{PORT}", "QGIS", Qgis.Info)
    server.serve_forever()


thread = threading.Thread(target=start_server, daemon=True)
thread.start()
