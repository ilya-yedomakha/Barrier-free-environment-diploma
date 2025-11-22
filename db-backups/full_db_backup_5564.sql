--
-- PostgreSQL database dump
--

\restrict kNIj013OlPc7hLWlFYnFOIVtAKXxqD509W167McCNaosYFHMeeYPTjhYZCeNCbb

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.6

-- Started on 2025-11-09 15:24:15

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 9 (class 2615 OID 48043)
-- Name: geo_score_schema; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA geo_score_schema;


ALTER SCHEMA geo_score_schema OWNER TO postgres;

--
-- TOC entry 2 (class 3079 OID 46618)
-- Name: pg_trgm; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;


--
-- TOC entry 5925 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pg_trgm; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';


--
-- TOC entry 3 (class 3079 OID 46699)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 5926 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- TOC entry 4 (class 3079 OID 46736)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 5927 (class 0 OID 0)
-- Dependencies: 4
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- TOC entry 1754 (class 1247 OID 47817)
-- Name: feature_type_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.feature_type_enum AS ENUM (
    'ramp',
    'elevator',
    'call_button',
    'tactile_path',
    'accessible_toilet',
    'parking',
    'entrance',
    'interior',
    'signage',
    'other'
);


ALTER TYPE geo_score_schema.feature_type_enum OWNER TO postgres;

--
-- TOC entry 1757 (class 1247 OID 47838)
-- Name: location_status_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.location_status_enum AS ENUM (
    'draft',
    'pending',
    'published',
    'rejected'
);


ALTER TYPE geo_score_schema.location_status_enum OWNER TO postgres;

--
-- TOC entry 1760 (class 1247 OID 47848)
-- Name: location_type_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.location_type_enum AS ENUM (
    'government_building',
    'business',
    'healthcare',
    'education',
    'culture',
    'transport',
    'recreation',
    'other'
);


ALTER TYPE geo_score_schema.location_type_enum OWNER TO postgres;

--
-- TOC entry 1763 (class 1247 OID 47866)
-- Name: moderation_status_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.moderation_status_enum AS ENUM (
    'pending',
    'approved',
    'rejected'
);


ALTER TYPE geo_score_schema.moderation_status_enum OWNER TO postgres;

--
-- TOC entry 1766 (class 1247 OID 47874)
-- Name: organization_type_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.organization_type_enum AS ENUM (
    'government',
    'business',
    'ngo'
);


ALTER TYPE geo_score_schema.organization_type_enum OWNER TO postgres;

--
-- TOC entry 1769 (class 1247 OID 47882)
-- Name: verification_status_enum; Type: TYPE; Schema: geo_score_schema; Owner: postgres
--

CREATE TYPE geo_score_schema.verification_status_enum AS ENUM (
    'unverified',
    'pending',
    'verified'
);


ALTER TYPE geo_score_schema.verification_status_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 226 (class 1259 OID 47889)
-- Name: barrierless_criteria; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.barrierless_criteria (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text,
    name character varying(255),
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_type_id uuid NOT NULL,
    rank character varying(255),
    CONSTRAINT barrierless_criteria_rank_check CHECK (((rank)::text = ANY (ARRAY[('high'::character varying)::text, ('moderate'::character varying)::text, ('low'::character varying)::text])))
);


ALTER TABLE geo_score_schema.barrierless_criteria OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 47895)
-- Name: barrierless_criteria_check; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.barrierless_criteria_check (
    barrier_free_rating real NOT NULL,
    comment character varying(255),
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    has_issue boolean NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_id uuid NOT NULL,
    location_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE geo_score_schema.barrierless_criteria_check OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 47898)
-- Name: barrierless_criteria_groups; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.barrierless_criteria_groups (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL
);


ALTER TABLE geo_score_schema.barrierless_criteria_groups OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 47903)
-- Name: barrierless_criteria_types; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.barrierless_criteria_types (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_group_id uuid NOT NULL
);


ALTER TABLE geo_score_schema.barrierless_criteria_types OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 47908)
-- Name: location_pending_copies; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.location_pending_copies (
    id bigint NOT NULL,
    address character varying(500) NOT NULL,
    contacts jsonb,
    description text,
    name character varying(255) NOT NULL,
    organization_id uuid,
    status character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    updated_by uuid NOT NULL,
    working_hours jsonb,
    location_id uuid NOT NULL,
    rejected_at timestamp(6) without time zone,
    rejected_by uuid,
    rejection_reason text,
    CONSTRAINT location_pending_copies_status_check CHECK (((status)::text = ANY (ARRAY[('pending'::character varying)::text, ('published'::character varying)::text, ('rejected'::character varying)::text])))
);


ALTER TABLE geo_score_schema.location_pending_copies OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 47914)
-- Name: location_pending_copies_id_seq; Type: SEQUENCE; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE geo_score_schema.location_pending_copies ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME geo_score_schema.location_pending_copies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 232 (class 1259 OID 47915)
-- Name: location_score_chg; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.location_score_chg (
    location_id uuid NOT NULL
);


ALTER TABLE geo_score_schema.location_score_chg OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 47918)
-- Name: location_types; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.location_types (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_group_id uuid NOT NULL
);


ALTER TABLE geo_score_schema.location_types OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 47923)
-- Name: locations; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.locations (
    id uuid NOT NULL,
    name character varying(255) NOT NULL,
    address character varying(500) NOT NULL,
    contacts jsonb,
    coordinates public.geometry(Point,5564) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text,
    last_verified_at timestamp(6) without time zone,
    last_verified_by uuid,
    organization_id uuid,
    overall_accessibility_score integer,
    rejection_reason text,
    status character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone,
    updated_by uuid,
    working_hours jsonb,
    location_type_id uuid NOT NULL,
    CONSTRAINT locations_status_check CHECK (((status)::text = ANY (ARRAY[('pending'::character varying)::text, ('published'::character varying)::text, ('rejected'::character varying)::text])))
);


ALTER TABLE geo_score_schema.locations OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 47929)
-- Name: photos; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.photos (
    id uuid NOT NULL,
    ai_accessibility_detection jsonb,
    ai_moderation_score real,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid,
    description text,
    feature_id uuid,
    location_id uuid NOT NULL,
    metadata jsonb,
    reject_reason text,
    thumbnail_url character varying(500) NOT NULL,
    url character varying(500) NOT NULL,
    moderation_status character varying(255) NOT NULL,
    CONSTRAINT photos_ai_moderation_score_check CHECK (((ai_moderation_score <= (1)::double precision) AND (ai_moderation_score >= (0)::double precision))),
    CONSTRAINT photos_moderation_status_check CHECK (((moderation_status)::text = ANY (ARRAY[('pending'::character varying)::text, ('approved'::character varying)::text, ('rejected'::character varying)::text])))
);


ALTER TABLE geo_score_schema.photos OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 47936)
-- Name: reviews; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.reviews (
    id uuid NOT NULL,
    accessibility_experience text NOT NULL,
    comment text NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    location_id uuid NOT NULL,
    moderation_status character varying(255) NOT NULL,
    rating integer NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT reviews_moderation_status_check CHECK (((moderation_status)::text = ANY (ARRAY[('pending'::character varying)::text, ('approved'::character varying)::text, ('rejected'::character varying)::text]))),
    CONSTRAINT reviews_rating_check CHECK (((rating <= 5) AND (rating >= 1)))
);


ALTER TABLE geo_score_schema.reviews OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 47943)
-- Name: token_table; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.token_table (
    id bigint NOT NULL,
    access_token character varying(255),
    is_logged_out boolean,
    refresh_token character varying(255),
    user_id uuid
);


ALTER TABLE geo_score_schema.token_table OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 47948)
-- Name: token_table_id_seq; Type: SEQUENCE; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE geo_score_schema.token_table ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME geo_score_schema.token_table_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 239 (class 1259 OID 47949)
-- Name: users; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.users (
    id uuid NOT NULL,
    email character varying(255),
    password character varying(255),
    role character varying(255),
    username character varying(255),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY (ARRAY[('USER'::character varying)::text, ('ADMIN'::character varying)::text])))
);


ALTER TABLE geo_score_schema.users OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 47955)
-- Name: verifications; Type: TABLE; Schema: geo_score_schema; Owner: postgres
--

CREATE TABLE geo_score_schema.verifications (
    id uuid NOT NULL,
    comment text NOT NULL,
    create_at timestamp(6) without time zone NOT NULL,
    evidence_photo_id uuid,
    feature_id uuid,
    is_official boolean NOT NULL,
    location_id uuid NOT NULL,
    organization_id uuid,
    status boolean NOT NULL,
    verified_id uuid NOT NULL
);


ALTER TABLE geo_score_schema.verifications OWNER TO postgres;

--
-- TOC entry 5905 (class 0 OID 47889)
-- Dependencies: 226
-- Data for Name: barrierless_criteria; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.barrierless_criteria (id, created_at, created_by, description, name, updated_at, barrierless_criteria_type_id, rank) FROM stdin;
17915a57-43bf-4538-831d-6fec7244410f	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	ширина пішохідних доріжок із зустрічними рухом становить не менше ніж 1,8 метра	\N	2025-04-12 11:25:13.505412	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
1f963a13-3602-4897-9c00-1ac8a31a6544	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	на пішохідних переходах із тротуарів і пішохідних доріжок наявні пологий з’їзд із уклоном не більше ніж 8 відсотків (на 1 метр довжини по горизонтальній площині не більше ніж 8 сантиметрів підйому) до пішохідних переходів (урівень із дорогою), пониження бортового каменю	\N	2025-04-12 11:25:13.505412	850a9b2e-ca6e-42dc-99b5-8ceb32539392	high
4346bc98-c82f-40e6-aca5-859b24c9a562	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	паркувальні місця для осіб з інвалідністю, завширшки не менше ніж 3,5 метра та завдовжки не менше ніж 5 метрів, розташовані на відстані не більше ніж 50 метрів від входу	\N	2025-04-12 11:25:13.505412	ebef7052-11e8-41d2-9132-d983c089a22f	high
43e8d998-b37e-4dc5-ba1b-cdc6fe3be41a	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	поряд із входом облаштована велопарковка	\N	2025-04-12 11:25:13.505412	ebef7052-11e8-41d2-9132-d983c089a22f	high
45085380-d245-4582-992a-92d989bf3524	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	відсутній турнікет, а за наявності — його ширина у просвіті становить не менше ніж 1 метр	\N	2025-04-12 11:25:13.505412	3d2623a2-9591-48d7-a8b7-eefbc8276352	high
5f0e64fa-f059-47ef-92cb-c8c36cdf009e	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	на зупинках облаштовано навіс	\N	2025-04-12 11:25:13.505412	e6099019-fbcc-44c7-ae23-007a9503e15c	high
515b9ef1-b363-41d5-9cfe-7bd43ddbae5d	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	наявна зовнішня тактильна табличка, що містить основну інформацію про будівлю або споруду, найменування, години роботи, розміщена праворуч від входу на висоті 1,2—1,5 метра на стіні або в іншому місці залежно від архітектурних особливостей вхідної групи	\N	2025-04-12 11:25:13.505412	dbe32e93-7736-4ad1-9cc1-b86772aa3b85	high
5b1a302b-b325-444a-8c38-d6d5cea6aa1a	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	наявна попереджувальна тактильна смуга біля краю посадкового майданчика та інформаційна тактильна смуга, що вказує місце посадки в міський транспорт (передні двері)	\N	2025-04-12 11:25:13.505412	e6099019-fbcc-44c7-ae23-007a9503e15c	high
65820f16-8043-4e31-97f4-529b53572867	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	відкриті сходи на пішохідних шляхах продубльовано пандусами	\N	2025-04-12 11:25:13.505412	0433893c-406a-4782-93ce-36a2a165dbf2	high
6589af3c-24be-4e9e-a0a6-38debaa8c896	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	із тротуарів наявний пологий з’їзд із уклоном не більше ніж 8 відсотків (на 1 метр довжини по горизонтальній площині не більше ніж 8 сантиметрів підйому) із пониженням бортового каменю до рівня проїзної частини	\N	2025-04-12 11:25:13.505412	fb235a02-48fc-4618-8871-5655ad80abdc	high
936c995e-86ed-4043-ae14-7bc96bccad95	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	майданчики для відпочинку розміщені не рідше ніж через кожних 100 метрів пішохідної доріжки	\N	2025-04-12 11:25:13.505412	67469639-a1de-4398-8f0f-2b19135e35a8	high
cca35f17-8d72-4da7-86a2-9a52f73ba13f	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	пішохідний шлях вільний від будь-яких перешкод для пішохідного руху, зокрема споруд, огороджень або конструкцій (споруд торговельно-побутового призначення, колон, різноманітних опор, інформаційних стендів, дорожніх огорож, стовпчиків, бетонних антипаркувальних півкуль тощо)	\N	2025-04-12 11:25:13.505412	8ea06582-4e41-4b7b-b4da-8ba0c92a3cac	high
e8faf9dd-6365-42d9-89ff-72c7c0b4b5b4	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	покриття пішохідних доріжок по всій довжині рівне, без вибоїн (без застосування як верхнього шару покриття насипних або крупноструктурних матеріалів, що перешкоджають пересуванню осіб з інвалідністю на кріслах колісних або із милицями)	\N	2025-04-12 11:25:13.505412	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
ee9c0427-904a-4ffd-a255-d04ca5336bc2	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	на зупинках облаштовано місця для сидіння	\N	2025-04-12 11:25:13.505412	e6099019-fbcc-44c7-ae23-007a9503e15c	high
fb3c16bb-7688-4076-8b68-1fc65e748589	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	паркувальних місць для осіб з інвалідністю не менше ніж 10 відсотків загальної кількості (але не менше ніж одне місце), місця позначені дорожніми знаками та горизонтальною розміткою із міжнародним символом доступності	\N	2025-04-12 11:25:13.505412	ebef7052-11e8-41d2-9132-d983c089a22f	high
fb44f5d0-3e84-45a7-9da2-d9ecec6d54e1	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	пішохідні доріжки, що перетинаються, поєднані на одному спільному рівні, на таких доріжках відсутні перешкоди (антипаркувальні елементи, клумби, бордюри тощо)	\N	2025-04-12 11:25:13.505412	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
293c553a-533b-4ff4-b8ec-5f2b24735fd3	2025-09-21 21:36:42.613149	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	test	\N	2025-09-21 21:36:42.613149	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
c3bf90e1-c8bf-4c2c-a86d-0a13d234facd	2025-09-21 21:46:09.607691	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	test2	\N	2025-09-21 21:46:09.607691	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
e5c016c7-560a-4833-a022-420f1a15d4b1	2025-09-22 09:21:28.328096	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	test3	\N	2025-09-22 09:21:28.328096	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
\.


--
-- TOC entry 5906 (class 0 OID 47895)
-- Dependencies: 227
-- Data for Name: barrierless_criteria_check; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.barrierless_criteria_check (barrier_free_rating, comment, created_at, created_by, has_issue, updated_at, barrierless_criteria_id, location_id, user_id) FROM stdin;
0	Бро	2025-10-16 09:18:49.767	76e28d58-f310-4341-9708-66d655db04f9	f	2025-10-16 09:18:49.767	cca35f17-8d72-4da7-86a2-9a52f73ba13f	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	76e28d58-f310-4341-9708-66d655db04f9
0	бембем	2025-10-16 09:18:49.767	76e28d58-f310-4341-9708-66d655db04f9	t	2025-10-16 09:18:49.767	1f963a13-3602-4897-9c00-1ac8a31a6544	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	76e28d58-f310-4341-9708-66d655db04f9
0	fff3	2025-10-24 09:41:27.053	4c88cc0e-b5f8-478c-928b-08cc12f38423	t	2025-10-24 09:41:27.053	cca35f17-8d72-4da7-86a2-9a52f73ba13f	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	4c88cc0e-b5f8-478c-928b-08cc12f38423
0	asdasd	2025-10-24 09:41:27.053	4c88cc0e-b5f8-478c-928b-08cc12f38423	t	2025-10-24 09:41:27.053	1f963a13-3602-4897-9c00-1ac8a31a6544	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	4c88cc0e-b5f8-478c-928b-08cc12f38423
\.


--
-- TOC entry 5907 (class 0 OID 47898)
-- Dependencies: 228
-- Data for Name: barrierless_criteria_groups; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.barrierless_criteria_groups (id, created_at, created_by, description, name, updated_at) FROM stdin;
410b96cc-c20f-456d-b861-609fbb443ed4	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Об’єкти благоустрою (парки, сквери, майдани, дитячі та спортивні майданчики, рекреаційні зони та інші об’єкти благоустрою населених пунктів)	amenities	2025-04-12 11:25:13.505412
28d11523-5483-4a85-bfa1-4ae409a73531	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Зупинки громадського транспорту (автобусні, трамвайні, тролейбусні, інші види зупинок)	public_transport_stops	2025-04-12 11:25:13.505412
6e4fb18b-5ee2-42cf-9bf1-c18987962166	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Об’єкти транспортної інфраструктури (залізничні вокзали і станції, автовокзали та автостанції, порти, причали, аеропорти)	transport_Infrastructure	2025-04-12 11:25:13.505412
9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вулиці та дороги (вулиці, бульвари, проспекти, провулки, дороги між населеними пунктами)	streets_and_roads	2025-04-12 11:25:13.505412
fb2cbe05-83eb-4923-abe8-cee76ce969bc	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Будівлі та споруди (об’єкти фізичного оточення — загальні будівлі, приміщення, споруди)	buildings_and_structures	2025-04-12 11:25:13.505412
\.


--
-- TOC entry 5908 (class 0 OID 47903)
-- Dependencies: 229
-- Data for Name: barrierless_criteria_types; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.barrierless_criteria_types (id, created_at, created_by, description, name, updated_at, barrierless_criteria_group_id) FROM stdin;
206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Пішохідні доріжки	PEDESTRIAN_PATHS	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
0433893c-406a-4782-93ce-36a2a165dbf2	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Відкриті сходи і пандуси	OPEN_STAIRS_AND_RAMPS	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
67469639-a1de-4398-8f0f-2b19135e35a8	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Майданчики для відпочинку	REST_AREAS	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
8499baed-c4c8-479d-92db-b85acb9b88a5	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Велосипедні доріжки (за наявності)	BICYCLE_PATHS	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
ebef7052-11e8-41d2-9132-d983c089a22f	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Шляхи руху до будівлі або споруди (будівлі)	ACCESS_PATHS_BUILDINGS	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
dbe32e93-7736-4ad1-9cc1-b86772aa3b85	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вхідна група будівлі або споруди (будівлі)	ENTRANCE_GROUP_BUILDINGS	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
365ea257-8428-470b-9da9-ee24ff8f300f	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Шляхи руху всередині будівлі або споруди, приміщення, де надається послуга, допоміжні приміщення (будівлі)	INTERNAL_PATHS_BUILDINGS	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
1238415a-a153-4062-af6e-f9e19f90f4a2	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вертикальні шляхи руху всередині будівлі або споруди (заввишки 2 поверхи і більше) (будівлі)	VERTICAL_PATHS_BUILDINGS	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
b297eb2f-6c9d-4ad5-a9dc-e49a292a8d76	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Шляхи руху до будівлі (транспортна інфраструктура)	ACCESS_PATHS_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
ff6f60e4-f5cf-4324-bab4-561ce0c18bd0	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вхідна група будівлі (транспортна інфраструктура)	ENTRANCE_GROUP_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
ffe36fe1-d1d3-4fec-84a6-77a3561ccbc8	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Шляхи руху всередині будівлі, приміщення, де надається послуга, допоміжні приміщення (транспортна інфраструктура)	INTERNAL_PATHS_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
901d9ccf-1b37-4121-9589-0c1fb0ec5975	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вертикальні шляхи руху всередині будівлі (заввишки 2 поверхи і більше) (транспортна інфраструктура)	VERTICAL_PATHS_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
28e76f14-75eb-4e60-b5c7-f6fefedbe472	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Безбар’єрність послуг у будівлях (транспортна інфраструктура)	SERVICE_ACCESSIBILITY_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
fb235a02-48fc-4618-8871-5655ad80abdc	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Шляхи руху через будівлю вокзалу/порту/аеропорту та прилеглу територію до платформ/виходів на посадку (транспортна інфраструктура)	TRANSIT_PATHS_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
3d2623a2-9591-48d7-a8b7-eefbc8276352	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Місця посадки на транспорт (транспортна інфраструктура)	BOARDING_AREAS_TRANSPORT_INFRASTRUCTURE	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
8ea06582-4e41-4b7b-b4da-8ba0c92a3cac	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Пішохідні шляхи руху вздовж вулиці (дороги)	PEDESTRIAN_PATHS_STREETS	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
850a9b2e-ca6e-42dc-99b5-8ceb32539392	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Наземні переходи, перехрестя і транспортні розв’язки (вулиці)	SURFACE_CROSSINGS_STREETS	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
f67d03fd-411c-4955-ac0a-a3de65e04373	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Надземні та підземні переходи (вулиці)	OVER_UNDERPASSES_STREETS	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
e6099019-fbcc-44c7-ae23-007a9503e15c	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Зупинки громадського транспорту (громадський транспорт)	PUBLIC_TRANSPORT_STOPS	2025-04-12 11:25:13.505412	28d11523-5483-4a85-bfa1-4ae409a73531
\.


--
-- TOC entry 5909 (class 0 OID 47908)
-- Dependencies: 230
-- Data for Name: location_pending_copies; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.location_pending_copies (id, address, contacts, description, name, organization_id, status, updated_at, updated_by, working_hours, location_id, rejected_at, rejected_by, rejection_reason) FROM stdin;
6	1	{"email": "", "phone": "", "website": ""}	1	1updupd	\N	pending	2025-10-12 19:38:20.726	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	\N	\N	\N
20	b	{"email": "", "phone": "", "website": "updsite.com"}	b	b(upd3)	\N	pending	2025-10-25 09:37:41.543	4c88cc0e-b5f8-478c-928b-08cc12f38423	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	9daf8549-6600-4b22-86d2-313ace15a0b4	\N	\N	\N
22	1	{"email": "", "phone": "", "website": ""}	1	1upd4	\N	pending	2025-10-27 14:26:14.321	4c88cc0e-b5f8-478c-928b-08cc12f38423	{"friday": {"open": "", "close": ""}, "monday": {"open": "15:11", "close": "11:11"}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	79a4e70f-2d1c-43d1-99cd-e3056ca96db5	\N	\N	\N
10	b	{"email": "", "phone": "", "website": ""}	b	b-більбо	\N	rejected	2025-10-16 09:20:00.414	76e28d58-f310-4341-9708-66d655db04f9	{"friday": {"open": "", "close": ""}, "monday": {"open": "15:11", "close": "11:11"}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	9daf8549-6600-4b22-86d2-313ace15a0b4	2025-10-27 18:35:59.649788	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	ніколи
\.


--
-- TOC entry 5911 (class 0 OID 47915)
-- Dependencies: 232
-- Data for Name: location_score_chg; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.location_score_chg (location_id) FROM stdin;
79a4e70f-2d1c-43d1-99cd-e3056ca96db5
4ac98e02-2dc8-44d7-b97c-2e2dbe4976ad
52820857-1fea-4f64-b31e-dcc3e8cc736e
aa127a96-a815-4f35-bb53-cae289d87707
\.


--
-- TOC entry 5912 (class 0 OID 47918)
-- Dependencies: 233
-- Data for Name: location_types; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.location_types (id, created_at, created_by, description, name, updated_at, barrierless_criteria_group_id) FROM stdin;
30c60f4e-b1d9-43f5-9fe9-74c1a3680d95	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	медичний заклад для надання стаціонарної допомоги, лікування та догляду за пацієнтами.	hospital	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
3e2e6e50-7a60-4bb2-919e-42d578d7159f	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	заклад громадського харчування, де відвідувачам пропонують страви й напої для споживання на місці.	restaurant	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
d531dc0f-86c3-4a9e-9d80-9d0479301e97	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	установа для збирання, зберігання та надання в користування книжок, журналів та інших інформаційних ресурсів.	library	2025-04-12 11:25:13.505412	fb2cbe05-83eb-4923-abe8-cee76ce969bc
905450cd-51f7-4f81-9381-b1ea3773dd7e	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	транспортний вузол, що забезпечує обслуговування пасажирів і поїздів на залізничному транспорті.	railway_station	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
18fc4aba-410d-4768-99c8-f0902327ddae	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	підземна або наземна станція метрополітену для посадки й висадки пасажирів.	metro_station	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
76795ab3-bda6-4e65-80b5-2911a51886a7	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	комплекс будівель і споруд для прийому, відправлення та обслуговування повітряних суден і пасажирів.	airport	2025-04-12 11:25:13.505412	6e4fb18b-5ee2-42cf-9bf1-c18987962166
91031674-1cf8-46d0-8d6d-2e8463003137	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	широка міська вулиця, часто з багаторядним рухом і роздільними смугами.	avenue	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
9aba00eb-5a2f-420a-8450-1e032f5183a7	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	елемент міської інфраструктури, дорога в межах населеного пункту з житловою та громадською забудовою.	street	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
0ee443d4-213c-4746-97a8-fbc788252761	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	широка вулиця з алеями та зеленими насадженнями, призначена для руху транспорту й пішоходів.	boulevard	2025-04-12 11:25:13.505412	9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8
a09b36bd-00b6-42a1-a9ba-df18fa94ec2c	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	місце зупинки громадського транспорту (автобусів, тролейбусів, трамваїв тощо) для посадки та висадки пасажирів.	bus-stop	2025-04-12 11:25:13.505412	28d11523-5483-4a85-bfa1-4ae409a73531
9eada58a-9d76-4e4e-bc65-0d434c3afe9e	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	зелена зона відпочинку в межах населеного пункту, що включає алеї, газони, дерева, водойми та зони для дозвілля.	park	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
dd8f63af-c5af-4f4d-8fdf-c9c9af0095a8	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	спеціально облаштована територія для ігор дітей, оснащена ігровим обладнанням та елементами для активного відпочинку.	playground	2025-04-12 11:25:13.505412	410b96cc-c20f-456d-b861-609fbb443ed4
\.


--
-- TOC entry 5913 (class 0 OID 47923)
-- Dependencies: 234
-- Data for Name: locations; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.locations (id, name, address, contacts, coordinates, created_at, created_by, description, last_verified_at, last_verified_by, organization_id, overall_accessibility_score, rejection_reason, status, updated_at, updated_by, working_hours, location_type_id) FROM stdin;
9daf8549-6600-4b22-86d2-313ace15a0b4	b(upd2)	b	{"email": "", "phone": "", "website": "updsite.com"}	0101000020BC150000C25FEB4EBC463F40E77ADEBA53C24940	2025-10-14 10:52:35.779478	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	b	2025-11-09 14:43:17.344801	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:43:17.344801	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "23:11", "close": "13:01"}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	0ee443d4-213c-4746-97a8-fbc788252761
7f26a8d6-a2ad-445c-8d11-868dd9811fbf	АТБ універмаг	вул Всіхсвятська буд 99Б	{"email": "", "phone": "", "website": ""}	0101000020BC150000F310722F27463F400EE8A29647C24940	2025-10-14 11:28:14.354553	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	дуже крута атббшка	2025-11-09 14:46:59.867319	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:46:59.867319	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	91031674-1cf8-46d0-8d6d-2e8463003137
d2280643-b7e7-481c-9f81-f54af2108042	АТБ магаз	Всіхсвятська	{"email": "", "phone": "", "website": ""}	0101000020BC150000C5B983951A463F402290ADAA49C24940	2025-10-14 11:33:19.322236	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	ага	2025-11-09 14:47:41.092108	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:47:41.092108	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	a09b36bd-00b6-42a1-a9ba-df18fa94ec2c
8ba45c39-e2d9-4b7b-8111-df98f22677dc	магазин АТБ	Всіхсвятська 99Б	{"email": "", "phone": "", "website": ""}	0101000020BC150000CC835DC725463F40F555B0364AC24940	2025-10-14 11:27:17.382366	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	магазин атб дуже класний і прикольний	2025-11-09 14:47:57.840432	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	pending	2025-11-09 14:46:09.830597	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	d531dc0f-86c3-4a9e-9d80-9d0479301e97
173eb32f-9109-4cfa-a844-5f378a141f20	admin's home(upd by me3)	admin's home	{"email": "abc@gmail.com", "phone": "123124", "website": "abs.com"}	0101000020BC150000F0F6F18ED50D50416E65F1D844533841	2025-10-19 10:21:06.508212	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.   \n\nDuis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit augue duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat volutpat.   \n\nUt wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore	\N	\N	\N	\N	\N	pending	2025-10-20 21:34:08.287966	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "11:11", "close": "12:12"}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "11:11", "close": "12:12"}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	0ee443d4-213c-4746-97a8-fbc788252761
79a4e70f-2d1c-43d1-99cd-e3056ca96db5	1upd4	1	{"email": "", "phone": "", "website": ""}	0101000020BC150000FC2A82B3204A3F401C76094C7ABF4940	2025-10-12 16:21:25.546875	4c88cc0e-b5f8-478c-928b-08cc12f38423	1	2025-11-09 14:45:16.946943	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:45:16.946943	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	91031674-1cf8-46d0-8d6d-2e8463003137
aa78e2a4-d82e-403f-ba0a-aa44b9fb80ae	магазинчик АТБ	Всіхсвятська	{"email": "", "phone": "", "website": ""}	0101000020BC1500009F917E321B463F40F2EBBCBA4CC24940	2025-10-14 11:37:18.054897	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79		2025-11-09 14:49:23.23708	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:49:23.23708	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	a09b36bd-00b6-42a1-a9ba-df18fa94ec2c
a09a55c5-6579-4cc4-a10d-e62fb761b291	АТБ	Всіхсвятська	{"email": "", "phone": "", "website": ""}	0101000020BC15000078F7B1BF1D463F4010C6A15E47C24940	2025-10-14 11:32:49.153866	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	норм	2025-11-09 14:50:24.497266	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	\N	\N	\N	published	2025-11-09 14:50:24.497266	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	9aba00eb-5a2f-420a-8450-1e032f5183a7
209ad370-6a3a-4111-8bae-cf534ee86f7d	bnbn	bnbn	{"email": "", "phone": "", "website": ""}	0101000020BC150000AB1F0410ED413F4000268B6861C04940	2025-11-09 14:06:01.689994	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	bnbn	\N	\N	\N	\N	\N	published	\N	\N	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	0ee443d4-213c-4746-97a8-fbc788252761
5210c86b-173d-45ab-b3af-2ee6469583b6	bnb	bnbn	{"email": "", "phone": "", "website": ""}	0101000020BC150000F82E64478D543F404248CFF81AC14940	2025-11-09 14:06:12.283902	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	bnbn	\N	\N	\N	\N	\N	published	\N	\N	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	0ee443d4-213c-4746-97a8-fbc788252761
ad074e43-7324-4cf3-b685-85485ec23e15	bnbnb	bnbn	{"email": "", "phone": "", "website": ""}	0101000020BC1500000EC9B311EA413F408BCD363C5EC04940	2025-11-09 14:06:29.414392	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	bnbn	\N	\N	\N	\N	\N	published	\N	\N	{"friday": {"open": "", "close": ""}, "monday": {"open": "", "close": ""}, "sunday": {"open": "", "close": ""}, "tuesday": {"open": "", "close": ""}, "saturday": {"open": "", "close": ""}, "thursday": {"open": "", "close": ""}, "wednesday": {"open": "", "close": ""}}	0ee443d4-213c-4746-97a8-fbc788252761
\.


--
-- TOC entry 5914 (class 0 OID 47929)
-- Dependencies: 235
-- Data for Name: photos; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.photos (id, ai_accessibility_detection, ai_moderation_score, created_at, created_by, description, feature_id, location_id, metadata, reject_reason, thumbnail_url, url, moderation_status) FROM stdin;
\.


--
-- TOC entry 5915 (class 0 OID 47936)
-- Dependencies: 236
-- Data for Name: reviews; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.reviews (id, accessibility_experience, comment, created_at, location_id, moderation_status, rating, updated_at, user_id) FROM stdin;
\.


--
-- TOC entry 5916 (class 0 OID 47943)
-- Dependencies: 237
-- Data for Name: token_table; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.token_table (id, access_token, is_logged_out, refresh_token, user_id) FROM stdin;
19	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNzY5MiwiZXhwIjoxNzU4NTI3NzEyfQ.Df0ve6Tir_l98rz9LC1VvXoLsWrms1ZeKWVFjcW_eftSjStQnq1LCQ4vXr5TW84B	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNzY5MiwiZXhwIjoxNzU4Nzc5NjkyfQ.ltEs1mZ0dKOqCT5lolW7BV7YcLEdMt5nmvgP78T8uIgjK_Mru6iVv_zEFTCrvlcc	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
10	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MDkzMiwiZXhwIjoxNzU4NTE2OTMyfQ.B3Tp8Vaon0etMDHK4BYWIXuALeVGV9lk2hOMrA3T9ig--NK9nmFge36m_Ldr1HQl	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MDkzMiwiZXhwIjoxNzU4NzMyOTMyfQ.imfcZOr8-J9a-qcJCJYc6B-hLY2g-TWAnBaftPkDPcbhoAno_LWbgnBDj2AnahUW	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
11	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MTE2MSwiZXhwIjoxNzU4NTE3MTYxfQ.mdpy5puoug42dFQXtXFYaveWMV-PLgUJhNP_scYQX9b1wkkM77pdw1el_u9O1gDZ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MTE2MiwiZXhwIjoxNzU4NzMzMTYyfQ.QAaibHyNWJCUdd2qC3CKP2n8UTlnYFHTsE_nNvTtdZqYzUM6YDugkyvPbB9BMjAa	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
13	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NDgyMjE5LCJleHAiOjE3NTg1MTgyMTl9.B4vDGGrCA9XibC_hVI8yfL-mJCoQHsMVYWGDXLeXIegqqxnJNtDry66uLGqoOha4	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NDgyMjE5LCJleHAiOjE3NTg3MzQyMTl9.GEgloZK8UllEdcc2rjcraKD-f_ABWYJgsCuguAIk1Z0Dw-cTRGZHSD895jO5Etc5	4c88cc0e-b5f8-478c-928b-08cc12f38423
14	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NDgzOTg4LCJleHAiOjE3NTg1MTk5ODh9.KvEPW53yRzITu7Zda-SuVtahDCaOS4VoaIfJ4xPOJZ0GvBChz-msRqJFkjNZD7B_	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NDgzOTg4LCJleHAiOjE3NTg3MzU5ODh9.vsnxPr70Z2RSgaHaPuIQeqOdP5bxWuBw2FLDwHnQJGJWW8pPRenEO0PD4myMz8k6	4c88cc0e-b5f8-478c-928b-08cc12f38423
15	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTI1NDQ5LCJleHAiOjE3NTg1NjE0NDl9.EX1i7zRAegbAK5wx95Si2XuS9EXg3cAddUvGkGKezme4baoIiOt3luqMGJPKNoer	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTI1NDQ5LCJleHAiOjE3NTg3Nzc0NDl9.2npPQic5g9360aW9W-IA7_uy0UKg4eCfhFhNmy6oHiUYVn9RBHKvyNuf6GAxmvr0	4c88cc0e-b5f8-478c-928b-08cc12f38423
12	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MTk2MiwiZXhwIjoxNzU4NTE3OTYyfQ.AAVXGFTuMmEtyTxklpdzNr3x6A5sUnB1N8FoTGqr7UmXo9HQ8q_fRFH3itDoMEx_	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODQ4MTk2MiwiZXhwIjoxNzU4NzMzOTYyfQ.aVMUA2NVsbU1cMGy8RpnSollVopEmSn2faHzRf2xRRSrXOdPmItVwobvMe9h-M87	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
17	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNTY2NywiZXhwIjoxNzU4NTYxNjY3fQ.nJ33d_FWowxGUMHZO-K_YdNd8X3Fmy18ITSpyi76lgpN6Mf7SDyMzZEqJtVjg0NM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNTY2NywiZXhwIjoxNzU4Nzc3NjY3fQ.wgnXqW1E6MC2uw_U9IJtnwCtne1a4aOr4euc-lTxa8T-c84Uwy77M-yEsR6ENndz	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
18	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNjEyMCwiZXhwIjoxNzU4NTI2MTQwfQ.yJlvukbwO3_P8eVwflvKlZa8iuRNlpwMdCy2I9AhbuaDwGmeD7AATHuyheaLDqCd	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNjEyMSwiZXhwIjoxNzU4Nzc4MTIxfQ.jxqvV9V6QDXJn2nf-GqrZTLitAcQHKDp8xkd_RuThkIvadKbHB4pOkZF5xmTU-A9	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
20	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNzgzMiwiZXhwIjoxNzU4NTI3ODUyfQ.Z2x51RimDzEBrjMNdsFiQm5fVmaaXRWY4kQEkWecFNRNTxxTcoj6XpGjQuAUR7xY	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyNzgzMiwiZXhwIjoxNzU4Nzc5ODMyfQ.Nw_XpP8kw81lGBhGrMgR4Yl8f5hmLOK8mWQXWDfFtqd-kxs_cCK_uCYFMk7AbdxR	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
21	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyODAyNCwiZXhwIjoxNzU4NTI4MDQ0fQ.I8QZmtFUnj4-pZlqyklP0jXk1ie32-v8yP061IJottbUlRt2G1Xq8GHtTgagWuyq	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyODAyNCwiZXhwIjoxNzU4NzgwMDI0fQ.K-T7CWgYn6YvmT6sXYWBE8hYKvEXiDGm8X2I8xw1IYUJAf8yhh5s-sKN0iL91NLx	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
16	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTI1NjI4LCJleHAiOjE3NTg1NjE2Mjh9._NzD_4g_LQobcGmoTlvL8wV9DiA4vqulhzVjcbgvN6SWU5sLeRxdwS8b9HzAu1aX	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTI1NjI4LCJleHAiOjE3NTg3Nzc2Mjh9.8GaVPxAJtczaV06AhGDBXW81jBS__8j6_7NcCLzSmb4Xv01jcIEQIrnd47-iWO3p	4c88cc0e-b5f8-478c-928b-08cc12f38423
23	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTQ2NDU5LCJleHAiOjE3NTg1ODI0NTl9.a95IwqXPpPjjfkZsrMS8ZPi0ReTWO-3jywj7T6m35TMScuPG83TyFVF34zwBTbW7	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTQ2NDU5LCJleHAiOjE3NTg3OTg0NTl9.zu49EunMdNVStYYYRIaN-McEvSkZd0N0DnVxuQcodaatm84v_bHudpEKxGK67WoC	4c88cc0e-b5f8-478c-928b-08cc12f38423
22	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyODU3MiwiZXhwIjoxNzU4NTI4NTkyfQ.8Zn1H64hDmXjcCguLYefVXA26PbzUMmriWr1UHybo4vWR7y3qt7SdqDIFhlcjFr7	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODUyODU3MiwiZXhwIjoxNzU4NzgwNTcyfQ.5JSXPLuoJOydbBJBIxV2ut8hcKXtDCt9tOXVm5OatZmyeUcdQUivd6m9ERKVdRn-	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
25	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0Njg0MSwiZXhwIjoxNzU4NTgyODQxfQ.5YoxkdiPKOKimSb7yue_uAyyYkrpMxy-mf79-tBSu1iPfnPSFg1IMy0AqA3YSoDk	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0Njg0MSwiZXhwIjoxNzU4Nzk4ODQxfQ.W7MsyWeyVH0T63qj7-N5d06zR8gzuoZLQGAkFh0-Z4sUDBR-3xSuVxYNkOAchmPJ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
26	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0NzQ4MywiZXhwIjoxNzU4NTgzNDgzfQ.ElEn5Frp7YPIQH9X2tkSxH4RN8700Ij7MwajGSzbbSURHvRRnwPO3_p2wGmMH2AD	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0NzQ4MywiZXhwIjoxNzU4Nzk5NDgzfQ.aM8dbkrPLGYRS8i4ZaaoaGKuOE3f59CJN__icQUVVUt64wQCaHKFKqsxcIdEZ5nt	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
27	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0NzY2NSwiZXhwIjoxNzU4NTgzNjY1fQ.epC7e_5M1n40WzDyg3qTcw-7zXvtl1H5JhWYe-tmbRkuzGy2vX8bDLmEyws-BJ2E	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU0NzY2NSwiZXhwIjoxNzU4Nzk5NjY1fQ.BJUbDv9dzEMBRTIpK5oLlq1Vjfwu9MTDQSgb3iW0ZfbpYgUIcnZYzPmLxEeWrg_x	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
28	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU3MTcwNSwiZXhwIjoxNzU4NjA3NzA1fQ.3Ac1WF_h3WaXOn2eSsLjgoMgXJuw4fGTxkO7xp7FJhHsrpmZ3-n4mpf7261ZxvcM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODU3MTcwNSwiZXhwIjoxNzU4ODIzNzA1fQ.P7MZY5_aq4ptbZQ3EQJQUGiY46o6so2_ersAHBp-wv6blzdFQaYlP9YGvoCnfXGh	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
29	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDE1MiwiZXhwIjoxNzU4Njg2MTUyfQ.Fhp-OwGGOFDi5J_fQiWfTjOj_fJ-4ktu_eOBlrin9CwOhc7-mURexzaZlJBrvJO6	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDE1MiwiZXhwIjoxNzU4OTAyMTUyfQ.BQXkwXzlUeJzJ2XASFu3t96R7JzYiwvYtFESkvLXtAQ54QBkR3uztrHjAHHMQmld	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
24	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTQ2NTkxLCJleHAiOjE3NTg1ODI1OTF9.1h7GZaNFWijUxKlO-dID5FKnLXOfdXP_YgWuaiDBgEC57eEoWwExRIBQEsVPYpEv	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NTQ2NTkxLCJleHAiOjE3NTg3OTg1OTF9.SJVxEo8eBJxHVpojuLG6cAVBqHJDhnH_KBTDLWj8YcEraE7lzDxEX5cWNPXNdqHc	4c88cc0e-b5f8-478c-928b-08cc12f38423
30	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDIwMywiZXhwIjoxNzU4Njg2MjAzfQ.tVdNtuGsXUTLuCO6KeG8329UE3Vlx_CX5VmmMjzenKvFj3YII7fQLxqMhtXaKY7m	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDIwMywiZXhwIjoxNzU4OTAyMjAzfQ.9ce98VR3kuh1ORmNB4ajmNFPujFb3YqK3vM4wfyF7oTSIqWmFrfpBY4IOWPW9CjZ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
31	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDU0NywiZXhwIjoxNzU4Njg2NTQ3fQ.wwZhcvkGANW-6tyK0hhwUpmh2KV8qPjCsILE2LZQU_pcjQk4znwGLEpukhlIB2Ck	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDU0NywiZXhwIjoxNzU4OTAyNTQ3fQ.u508azlLSkmtXokh8uES48XdSwgnaANnkfKCzTDUyS_wOx5OqZi-HuROJNto7qwJ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
32	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDcwNywiZXhwIjoxNzU4Njg2NzA3fQ.y5V_kly9p8Q6x_q5kRqz9glh22ztS3pqazQFWImWUfLDRNVbZdDZzPEDfMK2L6UJ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODY1MDcwNywiZXhwIjoxNzU4OTAyNzA3fQ.DV0LKhh9fnzAWq9xL-EdD7PYgkXrK4EfiJsD44J-dGTcX1VddomQ3n78m1GElMfn	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
33	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNTkxMiwiZXhwIjoxNzU4NzcxOTEyfQ.UMPN-dxzUNsyP4Rl1QjYYqcZlV33NYBEvEA1QE-m-MzmhOYI0saql3RrTlWCkFq_	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNTkxMiwiZXhwIjoxNzU4OTg3OTEyfQ.MepudEvxwozH_cHcbMYUQMmn3zAm8UB0Rn4uWGiIiyyBRpls7Qcg0G-zw8IZfdjo	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
34	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNjU0NiwiZXhwIjoxNzU4NzcyNTQ2fQ.NOJNTFlIEgWhonGV5kpJJpEo0ewxLJ4wD9bZLPzm3vsQgZ3vTIE-mmcNE49LXLco	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNjU0NiwiZXhwIjoxNzU4OTg4NTQ2fQ.L9G3CH6MmfseJ1zR0pJTcu13xgi0xUWn7vlmmYjf3iIsSHqBY9-uZPKYwxPkyVrK	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
35	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNzUwNywiZXhwIjoxNzU4NzczNTA3fQ.MpueEWmrJSf2oypnYNZ5_2iU1ecN2jGd_V4RD07bFQ3IeuhyLZ3DjBqoC1mVYun7	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczNzUwNywiZXhwIjoxNzU4OTg5NTA3fQ.6d8K81QaToBW79KKaOZ6SioIxKVu5pjQzk7HQGJPzUm0QhG7Be5dk65SRVLkmHax	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
37	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM4MTcyLCJleHAiOjE3NTg3NzQxNzJ9.hVYIhqiglbPG_EzNR1LknU9PBqi67Yj5-s5_kleqixXIVVbllJX3Mj7mlQTLJ16G	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM4MTcyLCJleHAiOjE3NTg5OTAxNzJ9.7O5NhPsiN-ntYIyDuu-lvDWjJEw7oL6M8ROTzZWOPi6szNxR2HGiibkV-qs3gW2t	4c88cc0e-b5f8-478c-928b-08cc12f38423
38	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5MjY0LCJleHAiOjE3NTg3NzUyNjR9.6hfLQ9mneVyZ6f5XaD3RJKzpaDFOwyB6eos1Ov2bTwVMC-YLAE_Tj-KB1GoiIjGc	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5MjY0LCJleHAiOjE3NTg5OTEyNjR9.pS16lKypCCfpwS4sJpns0BVMux4nacpWs87tVgBNscJbo3u5ohY6yv9Ev5VDmXCJ	4c88cc0e-b5f8-478c-928b-08cc12f38423
39	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5NTI0LCJleHAiOjE3NTg3NzU1MjR9.5ZryRWI0kTPBsLv52LjYseJ-bO8Oq5kOUoFn8A-cyAUhumcRDGbJFvJDIVWyoGA8	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5NTI0LCJleHAiOjE3NTg5OTE1MjR9.2K0oepQ5ZVh7V6Rhq1wCQa2-xEMveSV30aAbPWPjL69ip6JYDl-6dFPTWp6sP8io	4c88cc0e-b5f8-478c-928b-08cc12f38423
36	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczODA5NSwiZXhwIjoxNzU4Nzc0MDk1fQ.DwxS6SM3sGbz680rVUoH5cWdO_KoLA0TOmvVlQN0i_XH2FTpnFnOO9xaJSseMuAe	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczODA5NSwiZXhwIjoxNzU4OTkwMDk1fQ.cc_HusLN2_ZwXlkBOk8kEbFHjw352jr2xS2rmsK3Rjkwt59dIpkPI6mSkAZlpOVN	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
41	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczOTg2MCwiZXhwIjoxNzU4Nzc1ODYwfQ.VtsTmSLujXDDPJ4fvdKEOtC3arld58zCarBQs03UxM2jU6PmmU_Tfv4eYRqReA7A	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczOTg2MCwiZXhwIjoxNzU4OTkxODYwfQ.E1IoVyj_GsCcqmWNTMTpj-CwK_oN1wgN-yB13K3qpXxh1WN4nQipZru_c6ggQCHV	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
42	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODc0MDE1NywiZXhwIjoxNzU4Nzc2MTU3fQ.Ww0p7yDr1jGB0um41r3DLZpEoGt4VzuseAWGVruKwgNxwho3N5i6u92SDSCHqnTM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODc0MDE1NywiZXhwIjoxNzU4OTkyMTU3fQ.uYL5X5Qz_DbimrqDJbEU-1ZfhDdetmFLK7qsZm8u1ycygwIr2ZDHItMKzUl8_F2q	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
43	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDcyNCwiZXhwIjoxNzU5MTIwNzI0fQ.nW7G76Ycc2Pm3W31MlFpWmhXwZ17r6IL3ClztDMq1xPGUwWY14Dkj6LVhvAF34Pb	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDcyNCwiZXhwIjoxNzU5MzM2NzI0fQ.JuZ85wJ0CEZY_k8Q0qGdbV0853SZl8F2MQCwC9Vi26IAS8KIdD5YEEG7pZYIWshf	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
44	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDgwNSwiZXhwIjoxNzU5MTIwODA1fQ.n9nZ7Mm9UoWgAxbq1lpJDE8u6cwilBYHZ9-nlgm7ku9KpIaWzz6tDTVOIfdLxB_8	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDgwNSwiZXhwIjoxNzU5MzM2ODA1fQ.VfV9PLV2Ll0nXnUTHyJX1ompvUlw8cMFj1xacClRTfypgfORqKvRTehPRn9xwX8E	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
45	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTIxNjc4NiwiZXhwIjoxNzU5MjUyNzg2fQ.jAS16JoGWWj0Kjlm09U2hgkPSIgorzkj_ZRX2v1jtsN0enM9k1pMuRV6QLYw7CsP	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTIxNjc4NiwiZXhwIjoxNzU5NDY4Nzg2fQ.hn2yMvYP4OK6GQ2J80rlSg5b39dd0pXRphsS-hz-5BoPy4SAIintHyPH3zbssktC	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
46	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5Nzg1NSwiZXhwIjoxNzYwMDMzODU1fQ.xikVRoehdG6n-eBs1X5rFtK4JTCD4rRn9Mu317bvV4u7qwGyGZrkCW0ife02w7xz	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5Nzg1NSwiZXhwIjoxNzYwMjQ5ODU1fQ.HbyWmVfh2UBnnWtXQNxKYlaA765RyJ8DEqAMpoWqnVrhrwc6g52EFYrb1ts-nzur	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
47	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODAxMiwiZXhwIjoxNzYwMDM0MDEyfQ.TCzCX3YwzPTVx1h4BlPTwzpzw52BmJvpaS-NimgS2yygvVeZs8zB-YPAygGnAkiS	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODAxMiwiZXhwIjoxNzYwMjUwMDEyfQ.Q2174CDLTK9e_PRFLOE5M7YCXLCaKJO2A8Ur7bV84S9z8y54Y5mwfqkx3pd2ouVQ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
48	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODAzMiwiZXhwIjoxNzYwMDM0MDMyfQ.QbARnuEmuadDJcM_sEMq68apXh85VToRxpkvsoGyNZd0Tt2nPa5LamuhnYQKOn6c	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODAzMiwiZXhwIjoxNzYwMjUwMDMyfQ.ioCFmi-cixcu3Wi7Uo5SIWNl4-u5m3eLPwxUnOAD2mqyu1mznzyezde1bNoWslA6	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
49	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODA1MCwiZXhwIjoxNzYwMDM0MDUwfQ.1zjepq2ISMo7wblbCd5_QxvoDx8tCnq1exM8rR0jbhPeIlo6YJNr5CuSRgWb2mLu	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODA1MCwiZXhwIjoxNzYwMjUwMDUwfQ.UyAbt8-xZZ3n9hL7Vx-zJfHBKpUeW9M4UuZER1RuvE4yUoAQ038Fy1vZUdFMh2pc	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
40	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5ODIzLCJleHAiOjE3NTg3NzU4MjN9.v_IaWxQi2c2ylfihW1D5kcvWYby9KjmS-wFr56k20Y0P4DaRe63XMV2E-0M9uko8	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5ODIzLCJleHAiOjE3NTg5OTE4MjN9.wkg4Z53u3XEZjEIDDg1lJBgWdtTlGsGw6NjbOvp-RebDyEPD2k7kNIwdAek1bD-p	4c88cc0e-b5f8-478c-928b-08cc12f38423
50	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODIyOSwiZXhwIjoxNzYwMDM0MjI5fQ.JdnjN7c-IOfJ_1w_wUkEolxtZiFpbx0Jxz4o5HS7I6eXq44K9bhjpugaB1lG-B9m	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODIyOSwiZXhwIjoxNzYwMjUwMjI5fQ.XEFB0ebBnhH7Z1G335M6b_P-Qt231y6Z7TBy0VPjE6qV3Ysz4LS48D1BbSQpbZpF	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
51	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODI3NSwiZXhwIjoxNzYwMDM0Mjc1fQ.o__bKvHzHksja6mNUiwgvfjHdDYmgliufAiBETBUlMYahZ7xNsUMPsGl5T-0NORe	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODI3NSwiZXhwIjoxNzYwMjUwMjc1fQ._eU03VOUquFJXLtASvi8ZyoBmxaoZmmtgxcGkp77boRXmOQPxJQa20A1XUAdCmj-	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
53	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODg4MiwiZXhwIjoxNzYwMDM0ODgyfQ.Mh4H__jhdDI6q1V1fWTmHz_wNgjTqQXtxMExLYwMNz08SKorddexvnSlQnvG3KGX	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODg4MiwiZXhwIjoxNzYwMjUwODgyfQ.I5E8Y_NJ2UY6zwe3VB_BqIqydpDUDY2jCuObNRtBVTV6UtrHPLievsiYEJmoShV9	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
52	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU5OTk4NjYwLCJleHAiOjE3NjAwMzQ2NjB9.cKIBBxjjQN4Fq8nF3bXCuVRUO6pvL8zsx4HSqjUQqKu--bfGE7UwD2bR_CaiiH70	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU5OTk4NjYwLCJleHAiOjE3NjAyNTA2NjB9.YrLikxlwlN7v0BSpx7feem7rFbsJDsVlglKYb_k8p_yv7pPGEDIS68D5bKr14Cws	4c88cc0e-b5f8-478c-928b-08cc12f38423
55	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDExNzg4LCJleHAiOjE3NjAwNDc3ODh9.PKiMFXtRets83Hth3UVoqak88SJtUfrebt0gMBzPQRG8NGhzE_aY6BPO4UhI4eTV	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDExNzg4LCJleHAiOjE3NjAyNjM3ODh9.s4L8HBvGN1HtIpRuuQdUTUUfClkF6mPoNMHHxb-On2Yohmtf_1683XNDRGjeVctW	4c88cc0e-b5f8-478c-928b-08cc12f38423
54	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODg5MSwiZXhwIjoxNzYwMDM0ODkxfQ.UM9eDUmflWU92rPU49qvbf6mOof-h60PL6EgIsuHiPElTWevZO7TBOaQvfalyf-J	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTk5ODg5MSwiZXhwIjoxNzYwMjUwODkxfQ.3oxNfse1mo9e57TodLgSnOTXdJhhknc9Ln3KYaoelxhAKp7SsI_LlAcbc0oW7QTn	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
56	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDg2ODExLCJleHAiOjE3NjAxMjI4MTF9.Uy8cs2awv9ghQo-qOIESITMwyl_FQpe3z_wt_yie2xot1EEvotQwG3cy2bWDPlkQ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDg2ODExLCJleHAiOjE3NjAzMzg4MTF9.-YT4gj1T5BlV3pBp7PK-gwJxB0lerVR2ytLsYoMARAgOQd3edJwh57pN1c8x7j3f	4c88cc0e-b5f8-478c-928b-08cc12f38423
57	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA4NzMyMiwiZXhwIjoxNzYwMTIzMzIyfQ.ifNQ9muz-6N2sBt7Yq4jqb-fOAK0lGh-tGnbQ4I0WqjnOCK7vBSKjCMiMWekJNin	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA4NzMyMywiZXhwIjoxNzYwMzM5MzIzfQ.XseuN11lCD_uG3DuBFU8VPz7iU4wgkxxTlz4OLAr3ms6Csx-f7ug60GSIgril2hd	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
59	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA5MjgyMSwiZXhwIjoxNzYwMTI4ODIxfQ.vg_JRbKz8f2AWK0Oe4yOX0CDz1BKr9mAH3HFLmXrzQDBfvBQlKlbiAXT1cg4NK1-	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA5MjgyMSwiZXhwIjoxNzYwMzQ0ODIxfQ.an7j0Av7iQhic7vusMXNUiXkyH1tQthuYEGHlZErnwBj_zWbrGHHx4wkR7pCXkKU	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
60	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA5MzkwMywiZXhwIjoxNzYwMTI5OTAzfQ.2QH29xmD0EMhqLPaiodfHRn5Cb5qEmEVF9-F-GM6GGTMpm9fC1g6BZtg6drI_vwB	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDA5MzkwMywiZXhwIjoxNzYwMzQ1OTAzfQ.Cdo2Dsp3ZbEhpWFUeRLihgtpSa_GvBdoK7tkTAgM_1RoXSAipe7FmpG1haop1QIi	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
58	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDg3MzQzLCJleHAiOjE3NjAxMjMzNDN9.-J2U1BFs7kkLXB8aVNMFrODBRVT_Yq-Ci2GISHMBtgyrTigc5Kq1hjyd04TPF1BL	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMDg3MzQzLCJleHAiOjE3NjAzMzkzNDN9.RXua_Q8VJFU5MW1RDMPXdz8FX3IGU3F9HoNUgeNJXwsI0a1tyizQDqyKaV1YF_9Z	4c88cc0e-b5f8-478c-928b-08cc12f38423
62	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMTIyOTU4LCJleHAiOjE3NjAxNTg5NTh9.M0u6T0uReNrF3VLjJtT7mChrmQp1BTyRVApUN5sGGl1EZ1iB6qRL1syeTnSSzGRC	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMTIyOTU4LCJleHAiOjE3NjAzNzQ5NTh9.Gda27TR5n-vdWjAlEAMT2yZZ8SA7xDTwb64YMei-5akSBf3AuB-nAgY1MNno9Abe	4c88cc0e-b5f8-478c-928b-08cc12f38423
61	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDEyMjk0MiwiZXhwIjoxNzYwMTU4OTQyfQ.Zif_w9H4IUqQtn3jH6N7l7VvWuFbdH8YUZ4hoIPaSg1EH5futnuR1KVlSdqpixo6	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDEyMjk0MiwiZXhwIjoxNzYwMzc0OTQyfQ.xJCG9Yr73wxNgPRRWgbe8sMdM_tJlxdM4fAP6G2wqYphKLS92G_y9Rc_PVaIiRpO	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
64	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwMDcyNCwiZXhwIjoxNzYwMjM2NzI0fQ.LhcOQRL1QS5f5FNAht2NdW5bDOK6i7xWV2kWSaGDKQyLBwd3sKBpf4knsIkicrrz	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwMDcyNCwiZXhwIjoxNzYwNDUyNzI0fQ.pduLrmUMkRsNf5F6ZeEyaGKUL8AtZtBgADouh7KfzgvSWOc4BDHqtyM6ulaNdADZ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
63	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMTcxMDQwLCJleHAiOjE3NjAyMDcwNDB9.9wK6uiat3PNrLh4l2KC6Sw4O5b7S5_zs9OXWmWr4eraevHK_rLBUFtZllQSZNieD	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMTcxMDQwLCJleHAiOjE3NjA0MjMwNDB9.7igxpze6b8ff_72ktDhiyFvA7zP1msbWIVuUmeuf7XiAdv2ZIS8qFVExV5MWkJNT	4c88cc0e-b5f8-478c-928b-08cc12f38423
65	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDAwMywiZXhwIjoxNzYwMjQwMDAzfQ.1LBr6lNsQa1MIQ9E9QbNsju-hSIC4WeW3w1M4qG9qkReE2suWHmjvbp1etxpGdNV	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDAwMywiZXhwIjoxNzYwNDU2MDAzfQ.wkHy7pIWSUVwZSaKZYjE6YRfJiY5Bj2W7Iz_NTYL6u2_1iikpMn-iMXvIboj9T99	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
67	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDA3MywiZXhwIjoxNzYwMjQwMDczfQ.JRGU2HPFWP3Lj4RmIjzUriKoXCboju4NGAz27dsTYVDdgulqr78T7tkb0x6JGfss	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDA3MywiZXhwIjoxNzYwNDU2MDczfQ.woMMJWm6Q6h07Nqm_PiHQ7npTel1GEMfw_dicWsWmcqgqysjez75r9L4FMPCfRZx	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
68	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDEzMSwiZXhwIjoxNzYwMjQwMTMxfQ.Ni_WTr1Z4MZQyfVn1sJ7GCGKVhGTgWOQXPMKvDXJJq1H1qqitBssAcR8r8qdtXWW	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDEzMSwiZXhwIjoxNzYwNDU2MTMxfQ.fw-wEyAv_UpKAmM0Db0RsnMBNM6PEY1CLjTL1FTa-HBRvJv07XrfgMXSFpt6Gr3H	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
66	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjA0MDI4LCJleHAiOjE3NjAyNDAwMjh9.I90wqB2O-oonxPTKt3NYH7EI76B6QJ3xPllhcdrN20f-k1QILMGPidMFaAGDhL44	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjA0MDI4LCJleHAiOjE3NjA0NTYwMjh9.qXcrOFd6fMTqEOK2He5qL8U55Wn9cHtpEu_iX0oIZpClR9tWxNHAm2tUMAam_izS	4c88cc0e-b5f8-478c-928b-08cc12f38423
69	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDIyNSwiZXhwIjoxNzYwMjQwMjI1fQ.WfUZcVHMSsU0HXiXiBUKg7-xahiP5pYA0lVcWIzp1ivdbLTbvDSzFS9VY2mIZrNX	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDIwNDIyNSwiZXhwIjoxNzYwNDU2MjI1fQ.bDiZ-5fpYW9wDW15oK9YIENjhmERjk7QO9Owm8P-7q5rvtORTb9GUn2iGBFraAAq	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
70	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjYxNTI3LCJleHAiOjE3NjAyOTc1Mjd9.aJ-H2isC0VwtDQxS1P0-pILwamqHfv2_bBWEbs_f8SxnVy8G77hXKts4Ir7UpJc6	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjYxNTI3LCJleHAiOjE3NjA1MTM1Mjd9.n2nPu4tYTQTundYwysSZgZovf1FRqo_A5VrQFNhgvYqsGFI1zvSR8nBaG4S7Gku8	4c88cc0e-b5f8-478c-928b-08cc12f38423
71	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDI5Nzg3NiwiZXhwIjoxNzYwMzMzODc2fQ.WLQ1L7HcxJAcFeoo5MQU-mr7zQ0cdI_v4S_Ic2L51HGnv3uRc6P0QzeuSLGvpObH	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDI5Nzg3NiwiZXhwIjoxNzYwNTQ5ODc2fQ.wShVNnAdQCF7aeTUuGAvtOeR-Ghm0hToBtqyxek3uobFm4AFIOgry1Oymi4k1AG2	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
72	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjk3OTM1LCJleHAiOjE3NjAzMzM5MzV9.AUn0oR95E_GJ-5R9zCVD-KjtqT-9auLIggKMHUtbXNlK-TpDZByB1ZYWBoO80k9R	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjk3OTM1LCJleHAiOjE3NjA1NDk5MzV9.pxxzQq2mVA43eqFJe6H6Cj7If7dmdBWzm4RtEPBhHK9zcxY3G69ONZCza7n4oi7A	4c88cc0e-b5f8-478c-928b-08cc12f38423
74	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjk4ODgzLCJleHAiOjE3NjAzMzQ4ODN9.4s9RAlUKKCLYxr_CkXvHa8CsviL0Fz-5oolE4_ODF2551sdB-6LxxKVoonijXGh7	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMjk4ODgzLCJleHAiOjE3NjA1NTA4ODN9.Aw8FbX1E0M_KcNR24r9qTaTsyMTslL-qCxgskNQRnFSfLzzYpziMuC-oz7zCXfcd	4c88cc0e-b5f8-478c-928b-08cc12f38423
75	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzAwMTU0LCJleHAiOjE3NjAzMzYxNTR9.I1CjXWzuk1_K-eZW0gUB1DJiwsdJAfwVQDEx9q6qZCa6troQ-s4O4AjCyHaconWM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzAwMTU1LCJleHAiOjE3NjA1NTIxNTV9.53t1_QDWZfEWU8dYuIBL5ZTZblWQsBqgkFEDwF0kQlXvk_lq6KEdtDRFtPXiOOgC	4c88cc0e-b5f8-478c-928b-08cc12f38423
73	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDI5ODgxMiwiZXhwIjoxNzYwMzM0ODEyfQ.b-hTFfpbdtf9gEJz0jvnxGdPF4bMYp2oYPYKE_L4lLnIWIO5qZ-aTkk3kd_bLlwg	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDI5ODgxMiwiZXhwIjoxNzYwNTUwODEyfQ.dCLu6WZqBxIyM4EPibqWJmRDlmKPGvTniU7pfugav9sYruKelqQmHcBu0BM8rm3I	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
76	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzQ1OTA1LCJleHAiOjE3NjAzODE5MDV9._val_xLOBi-vJJMo5Qre-sEnWCnwwOkbIf1j6CxXa84LhA2gM9ESfiSowdWfgVqL	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzQ1OTA2LCJleHAiOjE3NjA1OTc5MDZ9.Rq4KlG-42GiiAFg85GWplMDTLQv5KKqUrzjfxyiGe50Jl4nS19igAc38cfB6cWN1	4c88cc0e-b5f8-478c-928b-08cc12f38423
78	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzQ3MDY0LCJleHAiOjE3NjAzODMwNjR9.bImJxRCRLG2LQhU1sJ9aqn13awFE_EYQD9ikqZbd9y4I0_5i39sNJZaRKU1PZ6Nl	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwMzQ3MDY0LCJleHAiOjE3NjA1OTkwNjR9.tuJ4x8c994AYiGF51SV0yHn_SSnkdkvdrC7ySlZ6WYzA4AiIva86NtdLgrgjVmw3	4c88cc0e-b5f8-478c-928b-08cc12f38423
77	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDM0NjEwMSwiZXhwIjoxNzYwMzgyMTAxfQ.qyLxSvSK-DS1KQg4tK-yOGjtDl8S1UL0RSxkWAzSw1Tr-ISBNImA5IHF4G_KVFNL	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDM0NjEwMSwiZXhwIjoxNzYwNTk4MTAxfQ.mvBAO_ZyKWPK3k-JbNyUG7WPGaZqbRnaOYUr-cnNYwcoNCc5thOf6gVQ3E4tuK7y	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
80	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDQzMTMzNCwiZXhwIjoxNzYwNDY3MzM0fQ.B-ER7GgfMRjoOhsSaFjUbk3THdIBWgVO6FPmAnwKB5Rp9JABg32JR0em2f2WJN9I	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDQzMTMzNCwiZXhwIjoxNzYwNjgzMzM0fQ.KN7-p7ibx8HMJxLAhdNv87VEFUKVHfeKvIGHkOu2yFHa05tUlSgWJfu9ZNd083Eo	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
81	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDUyNTY3NywiZXhwIjoxNzYwNTYxNjc3fQ.9f3afDC7JosJbMoQeRz8vHJymen58YzO1abNAgJXA_uskOix8Lvbvu9u0lPmSFaB	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDUyNTY3OCwiZXhwIjoxNzYwNzc3Njc4fQ.JMGTDzIT09LaYBUgzGvcEfgWtD5bmlQqCZouO3mcvtLJWfpuVMagdz_ibMWDtS0n	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
79	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNDI4OTgxLCJleHAiOjE3NjA0NjQ5ODF9.mgLzAq8ccTT0R-ednfttM8KEaCvP7-_3OwCgwT5xj_tiqPpLJx4duty2CU0OXmrk	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNDI4OTgxLCJleHAiOjE3NjA2ODA5ODF9.NEKoHSYqM1ZRaVJibYNEs9a9QjSyKksCVHlOmcQBiHggyc_I93Dv5seyyF-wd7U8	4c88cc0e-b5f8-478c-928b-08cc12f38423
82	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDUyOTY1NywiZXhwIjoxNzYwNTY1NjU3fQ.Vra8E2G3Qnr0arPAYqdiU7oB7LrmFlpJKnzAjPuHDuSZoTwjWHtLscui04TTa_Bn	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDUyOTY1NywiZXhwIjoxNzYwNzgxNjU3fQ.-hYEsIUZCwHvajfcPdyo6fV6dpcEITePttBLNTykAn-AicCFtpoztkNkppOLAPFR	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
84	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDU1MTc2MSwiZXhwIjoxNzYwNTg3NzYxfQ.KhZNtV11wM67IzPWRmRFxQzl14Ip8X2tgTjb3qOqCVgno0HnwvG1JPAnUSecY4Za	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDU1MTc2MSwiZXhwIjoxNzYwODAzNzYxfQ.ofM4nV9yMsrVVpyARkcaUg4r5QJ1hOSaweVKifxKozvLelgL41AVE-7CFL8j7J9j	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
85	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDU1MjMwOSwiZXhwIjoxNzYwNTg4MzA5fQ.JAKfqGDuAoqvZTQzx9GXvCAjUdqYuZE1Z3C9MwXwn42olLAcw3LgDgrKehzCZ9IN	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDU1MjMwOSwiZXhwIjoxNzYwODA0MzA5fQ.gKtixCMYWLjwgG-MPSqhSwRoevKGrJeuL0zQAt1Yc8-2aPczjH-0rDkdXi7wyvBE	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
86	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNTI2NiwiZXhwIjoxNzYwNjQxMjY2fQ.suTykl0Vv6aolNVK2lTZv3mKg0wsGv0fIwo8Uu3FygBvsdRGWaTCBGaOr-4bo2rj	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNTI2NiwiZXhwIjoxNzYwODU3MjY2fQ.hX2dFgzvFEk_oqiNBuzFHXWfHNgQOVHpp0J1J87G8vOhf66yNwVPtXqCqoGzBqMy	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
88	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYwNjIwMSwiZXhwIjoxNzYwNjQyMjAxfQ.-5sbFsqmAkyRWogyorOI10IVewJdAhVE293M4lSEWOepaaafIBJLBibgyvbGWtAg	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYwNjIwMSwiZXhwIjoxNzYwODU4MjAxfQ.Z7snqXsqiB9Inr_ttX8bckmLjmNaLtyMiAecWyklwr9GlndR9-Zz5AEkHHpALa1I	76e28d58-f310-4341-9708-66d655db04f9
83	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNTM3Njg2LCJleHAiOjE3NjA1NzM2ODZ9.zh7Ro1RqMNP_ojSj1uJ_IKAg04HfXxRFQOGCVDGPCa4lfBYoEyEP4MviziazDGKF	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNTM3Njg2LCJleHAiOjE3NjA3ODk2ODZ9.hfD2tCKYQsUYYvEjYKx4Dn2hEjBN9Fi5h2r7peP_JtPx0o_PjQA7RiEO7jxZ6zzQ	4c88cc0e-b5f8-478c-928b-08cc12f38423
87	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNTI2OCwiZXhwIjoxNzYwNjQxMjY4fQ.ffc1R7rNcdpmpaqf87d3uTuTb1IOWjm5_Cz0w--EbSCcM3qqhJ8v7Q7E1BigIthj	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNTI2OCwiZXhwIjoxNzYwODU3MjY4fQ.aEo8hNwcfG6fbi-WoyU5ihNhhE5oUp5En2KNEnA39flSKYJ1jzP8VTnYa00iOflD	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
89	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYwNjIzMCwiZXhwIjoxNzYwNjQyMjMwfQ.yWjyaBqJgXQ6-OEsWj7ZFDv5H1d2QjVDyjNayqjA4bzs1T69JJRQn_25bPlpVOE8	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYwNjIzMCwiZXhwIjoxNzYwODU4MjMwfQ.DJxUmWEFxZbCIzUlDbgtrMdPgFsHdsP106wkVvOCpP3sz-rdVjL5vqMxiHLNYdB-	76e28d58-f310-4341-9708-66d655db04f9
90	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjA2NDExLCJleHAiOjE3NjA2NDI0MTF9.5di9goza4bFM7jQe564kOoLZ4gpGLT6ugLPK5S4qgpnaxNgZdErfytO0680ieJHz	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjA2NDExLCJleHAiOjE3NjA4NTg0MTF9.ziUADJmjzijdGJeUaXLdfKo1PzNyTvAJWCAL3jJstfJpYiL7bWZRR6iwo8Zg4qXk	4c88cc0e-b5f8-478c-928b-08cc12f38423
91	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNjQ0MSwiZXhwIjoxNzYwNjQyNDQxfQ.zHLVoMozJtf97tK-voqalB8QzvIgHQkpABhBL-a0JFelugTcAF_BEeKtPjJ7NYuY	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYwNjQ0MSwiZXhwIjoxNzYwODU4NDQxfQ.9dgsH00ovKVB3FtA8WipvGQ6ujClUwakAvi7rVAAZdn2ctIkbFpvh6PWc7Z6nv0g	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
94	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYzMDA1NCwiZXhwIjoxNzYwNjY2MDU0fQ.CVZ4iW0j1b1DD3icKIwMTaqxsQ8XuOe24dKpcczXLFbN0PZa-uoz-4zMTrszAfOd	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYzMDA1NCwiZXhwIjoxNzYwODgyMDU0fQ.NHwnkZzA7kIOrB_Lri1qu_fHdJC9Is373Fh1cRnYw7zvZ7Ny_EZZBbyfBlbc2ZMG	76e28d58-f310-4341-9708-66d655db04f9
92	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjI5OTY4LCJleHAiOjE3NjA2NjU5Njh9.-tOoKQOjGoP65LJsn8HClBLpdKPIUyVq99Cy773UL-OXuGt2GEDEFQo4xzjEG4e3	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjI5OTY4LCJleHAiOjE3NjA4ODE5Njh9.IUlNw3zGATVGgU5nH1qTRapaDTr4OfNo2TH43lMtNQf1iV8iergrvqOq-tsJzqMt	4c88cc0e-b5f8-478c-928b-08cc12f38423
93	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYzMDAzMCwiZXhwIjoxNzYwNjY2MDMwfQ.TC_XR7onZACavL_hpxbnuom1BlT0jzNiBuMJ4OWOLcZfbBd55K9_NSQj45mA4yNo	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYzMDAzMCwiZXhwIjoxNzYwODgyMDMwfQ.IMeMD1BjPZs7LcV8W6oq0swd2xf-Kxc-yUTwirzUqjyRgivM0yJQ99gX3j0SJI2O	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
97	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYzMTE0NSwiZXhwIjoxNzYwNjY3MTQ1fQ.8kQGrvTi_8p7JC7DHSe2rW-CzuKAG4N3HKAbdG2oZjxNSsHFZoQw6SaeQtCzX9Vw	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDYzMTE0NSwiZXhwIjoxNzYwODgzMTQ1fQ.w2qyGPnKfaskvVBguyO8F2r25QJzrjSd7p_oTn9oMaxwMt-iYva4kbceu9ws62rI	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
98	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDY4OTExNiwiZXhwIjoxNzYwNzI1MTE2fQ.HXvng_zwye9Dgs4BW4ICmi22c1nvtopPnhsqD87N5nk4CmK8dO8lfXlSQdjDTq_O	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDY4OTExNiwiZXhwIjoxNzYwOTQxMTE2fQ.XPjjCE6CMRjdcFAaCndBbNxDsv5tMccCpwb6HjFBOCmJl_PIy8dho4HYUGAqZVPd	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
96	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjMwNDUzLCJleHAiOjE3NjA2NjY0NTN9.2MpI73Rkn6x8yWiRgCifLq7Hg9ViOxaRkJmwl69hnjDwo_Z-Agelb88iU00YxYAM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNjMwNDUzLCJleHAiOjE3NjA4ODI0NTN9.OorEIB5Je_hWz3AgGhJV-RQB48XcxhQtC14iInBenAEDUNiIW7NRLNjcQ5DtTW6_	4c88cc0e-b5f8-478c-928b-08cc12f38423
99	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDc4MDA3NiwiZXhwIjoxNzYwODE2MDc2fQ.vrOsDs0PVJaEmbRsz6wEAUx7yS9G-MO3n5xX5rxUkYBe0mLwUOocUpT_t5NB4Iq0	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDc4MDA3NiwiZXhwIjoxNzYxMDMyMDc2fQ.zc7HJKL6jFS1ZQUVBBjR51w5TtBY7uxLi9cFgvRWaKTrKyXG8sgeX8mEgkkYD6bI	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
101	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDc5NTI2MSwiZXhwIjoxNzYwODMxMjYxfQ.gifu5LfJkNt1Ym5fO0krMUFhzDkXl0VHBdo5Tz8xHdCyh1f5cqCp8W0_Ip4JmcJH	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDc5NTI2MSwiZXhwIjoxNzYxMDQ3MjYxfQ.b_FsklaPvxQUwARHuwpVU7MHdwSMGlfEBKp469QaQqi7HpCnvY1HQp-RvJyjcr2q	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
102	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDgwMDc3OSwiZXhwIjoxNzYwODM2Nzc5fQ.qv4F9ft5rY82I3KSlAOSPgSOarBaDWWaU2l-duknXVXpPwgPiiBprE6GmEkUntVK	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDgwMDc3OSwiZXhwIjoxNzYxMDUyNzc5fQ.fEYWp3W_owzaghuPZmMsX9Zd-LyH4gBQrGneTF7X6LenWjTIZIPSRmxrQmQ44BGm	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
103	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDgwMDkwMCwiZXhwIjoxNzYwODM2OTAwfQ.3v3fQbTYoe4U14DgIfxrNAD6VvEtmil6Wzoh0f61skovDJ3ZwfTNlRvKBD38RD4Y	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDgwMDkwMCwiZXhwIjoxNzYxMDUyOTAwfQ.jYSFeRyxk_uj59YZxOEj_tMDM7g4cunCQaHS4vDbsdgKTeV-WYYymL_3oJ4pWPGK	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
100	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNzk1MjIyLCJleHAiOjE3NjA4MzEyMjJ9.LN9nb7HY1o6krrG3lAMmJvhd6SA4E3hLMilfFu7hiSVqq_LuLi1fHUXHM8aHYfXn	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwNzk1MjIyLCJleHAiOjE3NjEwNDcyMjJ9.jW54lEXsdfUO28Z-p6NN5SFXoJyYBzLUeMnE8UeHHsM4nB67Ft6yArsvrkSavX5O	4c88cc0e-b5f8-478c-928b-08cc12f38423
104	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MDkwMSwiZXhwIjoxNzYwODk2OTAxfQ.fyEc73oC4N5dICgPyZhA-hQyu0JNeA8Yrr5YGwqmGhGVsd98eStuoLMeH8kKRQCt	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MDkwMSwiZXhwIjoxNzYxMTEyOTAxfQ.WlBxrWQH-VJGC2htMFsWSjPq5yO6dUQ-7yCpk58oteRWmSLymjv-KOfe45y_p5or	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
105	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYwOTYwLCJleHAiOjE3NjA4OTY5NjB9.ipEGkim3oek15aHtmM2MUJf9pSmxRQSfFOk-6GZVwU1V9DYmLlB4Cre_ee-VdHKm	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYwOTYwLCJleHAiOjE3NjExMTI5NjB9.Xe39BvwCHqxBKE6zfk8biu65AWicUaLp-29_FoE0xNv1-HPLWTCsz0l6vqjdPONV	4c88cc0e-b5f8-478c-928b-08cc12f38423
106	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MTgwNCwiZXhwIjoxNzYwODk3ODA0fQ.Y_wxBcnKqH9fnXqdKBZR1iBez0HILAWprBSZpq6grwc0y8aej8cdzBfSLFVZ5Ay5	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MTgwNCwiZXhwIjoxNzYxMTEzODA0fQ.hqO1OgmqqyjPmgqHdsC453l6qjZavpef09qNOy8bzDCG-r5qm1C4XHvMeJ6jnt3E	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
107	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMDkyLCJleHAiOjE3NjA4OTgwOTJ9.rcHVWMq3tDyqw9sfGy7llvyaHbUTz0O2h9G-JiJ2GSNN0zcCB0vfoJnimuNILViF	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMDkyLCJleHAiOjE3NjExMTQwOTJ9.ikPBMcJjF16EsUcE4u3mwZxFRXub86osyI_0zcA9yBXAb9QsMKDuKjBqin-WvW1t	4c88cc0e-b5f8-478c-928b-08cc12f38423
108	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjEyNywiZXhwIjoxNzYwODk4MTI3fQ.sIgMhBWXtaOcMHWg0QRRm2qrJ9aR6yqJQtiQrEgUW-AcrIAGBHXstjk1nPSQ0uUJ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjEyNywiZXhwIjoxNzYxMTE0MTI3fQ.6fJ_80pxNbTX2cuwpq6SawXWOdhxbCHtFbOy-VtPbnTe-SCFNT68yAkocIt5rw9P	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
109	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMTUxLCJleHAiOjE3NjA4OTgxNTF9.bBXUxKar7LtgssLxo5nfP1I48E7D8MgOgyOBNvtLgGZwI07tlcOitubc0RDNBE7V	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMTUxLCJleHAiOjE3NjExMTQxNTF9.Hq6PI928H8QGD5KvEuefF7Ki69U8T4IbDs2zib08HbKFVZfKt_oywBmz5rXp4Dde	4c88cc0e-b5f8-478c-928b-08cc12f38423
110	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjE2MiwiZXhwIjoxNzYwODk4MTYyfQ.mvZOsyYllOn9_8gjyTebMojo6oAIIBOOpHMSgysKgFVOvjiDDFaKvHpPACZ9yClp	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjE2MiwiZXhwIjoxNzYxMTE0MTYyfQ.VA05-go7ninMs2IigemjdC5myku-8kl_pxa1zRqk22cc_AFB3ryt8gqdN10ZwZtD	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
95	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYzMDM3NiwiZXhwIjoxNzYwNjY2Mzc2fQ.iik_pbkOXkhELRJ2t_DUaPiGn8AY9ImV9UKkwslARVqMmUUrdxstnwSgOa9LgyxG	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MDYzMDM3NiwiZXhwIjoxNzYwODgyMzc2fQ.wIRXqdnJksRNIxK5R_kDdaUAea2gyAsmKftEzm7eqve8Jphf64SxEh5fAjERaarH	76e28d58-f310-4341-9708-66d655db04f9
111	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMTY5LCJleHAiOjE3NjA4OTgxNjl9.uOTAynXUaRUAR79_zY2eOSSDfFbgcbD9YRH4Q4np8LzoW9jZNDLWnllrHA8tiju5	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYyMTY5LCJleHAiOjE3NjExMTQxNjl9.zk184kOb2HD_cRbD5YmfJuiia-5PGy4Y1aGrx6jZxX9-43901EYJwbLlkQBz9yCr	4c88cc0e-b5f8-478c-928b-08cc12f38423
112	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjI0OCwiZXhwIjoxNzYwODk4MjQ4fQ._fm-6ym2DkxXfxFTzO6Cul5faP_n0ZlcGxIQQgHtfQSs7TNJdsogF90HnHk_2wC5	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MjI0OCwiZXhwIjoxNzYxMTE0MjQ4fQ.WpqyhL62zTraW3kyWDNyO0uEKgA_L1Q-b76u5RvAFk4DFh08rGagu-bljzZVvRD2	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
114	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MzE4NCwiZXhwIjoxNzYwODk5MTg0fQ.coJvYBXLDp2Awy_ZqNM5R1WDhOEAgKqaHoXa8TG2wZIz3CyZUdkYQ0g39SdgS2_J	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg2MzE4NCwiZXhwIjoxNzYxMTE1MTg0fQ.mhlOlqxQGFXcWSB-Z_gUg_eDVTqMtRAKTm7B4O_m4khRSAIuHHP425WtZx2_3m6V	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
115	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg4ODI0NywiZXhwIjoxNzYwOTI0MjQ3fQ.YEkbe0m1vYmwcTCCBNfKIi0ppDkCM93CxlDxJWNgS_B6W1vAiyUObIsP34UVe0Oz	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDg4ODI0NywiZXhwIjoxNzYxMTQwMjQ3fQ.kySrh6NiPrOKFp9AtTJCNH7TQCi8wyqe3bFTERTX_z0V6t1MZLhRjjG-y9_hBLFT	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
116	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk0OTgyNiwiZXhwIjoxNzYwOTg1ODI2fQ.UBq4eMPga6pawD3rituYEJC5s1OejhME3S-_2ZXhTJms3XmYrN1g0L_bPK20Pz7b	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk0OTgyNiwiZXhwIjoxNzYxMjAxODI2fQ.YWQQmokUkL7CsqEQeUfqChQsmcLu6P0x2Fe4x8eGVsi9GsAN7ZjJcQteaE557HCs	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
113	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYzMTA2LCJleHAiOjE3NjA4OTkxMDZ9.ocr_-qBSP4A4PysGnbWO3bnMtzM0D-KLzQbcaGOabEGp-eyd6d9XHzGGdxHaV9jF	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwODYzMTA2LCJleHAiOjE3NjExMTUxMDZ9.Rlwif3LGwS7Q4dL2OJB5PJiMViYWeJNqH0TS_gMuFsreEBw3IupXivppMwssaxgC	4c88cc0e-b5f8-478c-928b-08cc12f38423
117	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk2ODEwNywiZXhwIjoxNzYxMDA0MTA3fQ.MTjI-unQgGYl79gMkIbT6cxco7oOY_4XsjqLX0MQ3G8_b1YPn4QXavJm1TlApEp1	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk2ODEwOCwiZXhwIjoxNzYxMjIwMTA4fQ.BgimldG1w0tW-uB4p3aiVclN7MvmdsLziDdRyhITGfdc1P7SUU_j_yFFSwlmEF9O	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
119	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk5MTEyOSwiZXhwIjoxNzYxMDI3MTI5fQ.vklduJ1jjlX9Nmj0gZJttZ2vG8cPhARmCfoe2tH0pB6rqjnoBIQbiWtXtl23v8cr	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MDk5MTEyOSwiZXhwIjoxNzYxMjQzMTI5fQ.0C4kjiTYSGU-JlTIFtMgwXaP2KHLXzQkt_xfQEAd_u7roQKkPp9o8mvA7uGMTbFI	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
120	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTA0OTg2OCwiZXhwIjoxNzYxMDg1ODY4fQ.fS2HQCBi0seguA55zUC3QiFULAn-ZAXYwtS-ssV_1ATaMaz_GQZAAVq8UIDiXLP4	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTA0OTg2OCwiZXhwIjoxNzYxMzAxODY4fQ.35zSYwcsg1AbA6svaZEJyin20AoBbHz9xe61z0AJ_5zeJBcLeEK8Wa7Ti0OM1Z0V	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
121	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTA2NTAzMiwiZXhwIjoxNzYxMTAxMDMyfQ.q-gYt2BB7HbreG2Y2o12r4DU3CAY5Xgukd8NcAqDsm-qwA1BciAtIe5iDQuYn7fH	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTA2NTAzMiwiZXhwIjoxNzYxMzE3MDMyfQ.2walNs0kPXuSDusTE8HWe1LbUmqWSyML3hym5cX4R78_ya-TNQ9LqeSGP4Ml2Fqo	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
118	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwOTkwOTIxLCJleHAiOjE3NjEwMjY5MjF9.W2MvpskAVV6ILdCFGcjMeG9BcuaT_bXgQfzYWe-exsGerkH9-roidbbKJeIhhwx0	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYwOTkwOTIxLCJleHAiOjE3NjEyNDI5MjF9.ppx-HGwVjTK4oTgFvhRZBm-s0CYjTasJdmqldK_weHnDXd_pPxs_AiaCFKiOI1AT	4c88cc0e-b5f8-478c-928b-08cc12f38423
122	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTIxNjkzOCwiZXhwIjoxNzYxMjUyOTM4fQ.zAZG9oHF36Np22ZAQmksK3OqOYl3EbMCha8oNhu4EzhaDlHmKjYcTy8ySeL_WGzE	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTIxNjkzOCwiZXhwIjoxNzYxNDY4OTM4fQ.ybmeYdaVPhCMZ2DV4UbZPZd33Ge-CMZn1Ud_cK5JhXYAR1vbYbkFZOxajaHN_Z40	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
123	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjQwNzcwLCJleHAiOjE3NjEyNzY3NzB9.B3UbHRd9B4W_xkxsXh7pSIr3W2FlKk2LVQC0yaZb6htIoBVtU5hBzS2cDaYquXos	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjQwNzcwLCJleHAiOjE3NjE0OTI3NzB9.gv0JX-Y0gRRPo6f-jhqsO4NbKWtpKTlurm-aJnKopIQTZHfLDid02G6ALaNpueDx	4c88cc0e-b5f8-478c-928b-08cc12f38423
125	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjQyMDU3LCJleHAiOjE3NjEyNzgwNTd9.tvmnqHz0XkE9WmXueN0a-d5X8-E28gG0lKAsJ4XA4qMZQopUzoK75jwxBmWVDOKJ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjQyMDU3LCJleHAiOjE3NjE0OTQwNTd9.hPS0SNffNXpj-ctPIFGTRQzRbqRkNiZ-FKsJv5dlbc6j2NwDc18PDk39y0Pz3tSD	4c88cc0e-b5f8-478c-928b-08cc12f38423
124	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI0MTQ4OSwiZXhwIjoxNzYxMjc3NDg5fQ.Au9kMUKt61MNX9Xk521RwX2y7olRfMKO-EiDCIYMAmC8Gi6SA6K1T-71tOy81qMN	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI0MTQ4OSwiZXhwIjoxNzYxNDkzNDg5fQ.IHPeFZgSYOtuZBbma8wj1_kgs40B0A3KYa1_o7JwwGvC-yakYb4p2j_Oh-tJ7cKC	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
126	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjk4ODUyLCJleHAiOjE3NjEzMzQ4NTJ9.IDaV-VPLpkjmlvrUQm3eGpGbFpZVPjRqEYb0jo36nxJe3DVhXcx8QEs6MrAA1dMe	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjk4ODUyLCJleHAiOjE3NjE1NTA4NTJ9.eJety_JvvVoxtaoqt9kdn1iOYn5IdLxnvZhnzokLqUNoKTgAaZ5UEBNc-B2lVJcj	4c88cc0e-b5f8-478c-928b-08cc12f38423
127	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI5ODk2NywiZXhwIjoxNzYxMzM0OTY3fQ.AgSSGQTCqu_B3__dhCBpWcNlGhq1I7H0LQIg95jzkKTxGcxojKwvCiJ6fRJ-VNJj	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI5ODk2NywiZXhwIjoxNzYxNTUwOTY3fQ.hkswLIslzsSDnAsJMMRN-Qi1ZoAxbAXZoXgRz6lMSXzrRrkVWEj-OdLG6hMaZjfD	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
130	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MTM4MDQ2MCwiZXhwIjoxNzYxNDE2NDYwfQ.97EEDtjnJfVxr-fy6g-yNkhuyLlr3xIvmNH47f_CfwI1v5uQ-MSyKSlv01e3nSob	f	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJiaWxibyIsImlhdCI6MTc2MTM4MDQ2MCwiZXhwIjoxNzYxNjMyNDYwfQ.PThV5rgfamzw3G8ubMIzL4uh_hV8ebRiw7Oo_wg2HGdrsEsXCJWIgB-1lwd8fIJR	76e28d58-f310-4341-9708-66d655db04f9
128	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjk5MDE5LCJleHAiOjE3NjEzMzUwMTl9.z19iCq-e-jYpxw8MWMGLzn_PQDLlZivKyJqq5VpkQ2uQTdH1ocoSHUMEpZr5Ay7L	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMjk5MDE5LCJleHAiOjE3NjE1NTEwMTl9.rHykcKd16OySdcXrZdWSfsG9K_hSsFgCQvsGedjg-nYDfN7KX-X8Ygq4LFXw-jL-	4c88cc0e-b5f8-478c-928b-08cc12f38423
131	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzgwOTg1LCJleHAiOjE3NjE0MTY5ODV9.xIB1YdT_mVS8PAIm2RMvu96gcahPreHy8LFzBgJnbvKsmB-WIsR5FE_9MdLCrXO4	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzgwOTg1LCJleHAiOjE3NjE2MzI5ODV9.9ufKeGmyWQ2Bf5ZmeIR0hge2Xx848MS2HFveWUwH--iqsEwi9PYc6915xCUqbQMP	4c88cc0e-b5f8-478c-928b-08cc12f38423
129	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI5OTA1OCwiZXhwIjoxNzYxMzM1MDU4fQ.LrWZHyjJKVXrBw8Gvx2jqGHi8FGNwmzFQlf-5ENStZyuKmmx4Zzu76TxpPvdqEGO	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTI5OTA1OCwiZXhwIjoxNzYxNTUxMDU4fQ.j-Sq1ubwRbW4RozOdZ7Y70BE409QF41oDOVzYSEkAkBO71W2O4aVkrsxFOeUWWGQ	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
132	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzgzODI4LCJleHAiOjE3NjE0MTk4Mjh9.OosyJUgQSNjTsF5IW_I1PXYpG0FCfo09BK5isezSZqwPhaA8GzljIDLLk_8IAZNw	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzgzODI4LCJleHAiOjE3NjE2MzU4Mjh9.cnFC5xVFYkeBfCAHCBSv1GO71DProVQjd5SpZcccStqqgXLnkVJDxJLM5vxliB9Z	4c88cc0e-b5f8-478c-928b-08cc12f38423
133	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg0MDY2LCJleHAiOjE3NjE0MjAwNjZ9.oGDE7gJcwHeJbIVgDANvgZOwsTmPFooaNcnRTud3RYV-iYJDUkL1eO-lt_zDw08W	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg0MDY2LCJleHAiOjE3NjE2MzYwNjZ9.mtIoN9TbpfYKl1H7eEF1UA8QDRUaz3JuEUJ3tKYYpN-UNL0ls8Uv0L4pHL3HgY_r	4c88cc0e-b5f8-478c-928b-08cc12f38423
134	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTM4NDExMywiZXhwIjoxNzYxNDIwMTEzfQ.6bxbJHjakEcQjyLYcEqQ77k2jM99IUyqLy-W0j1Qpx7jF0cnKfX4xOnejN2H7zBw	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTM4NDExMywiZXhwIjoxNzYxNjM2MTEzfQ.Q2iBD-IaVWk7Ah1gHHiVlP81R6YlMZjgNLcEZNJhSVehsa9Hy3EvrP2TWePxqRe7	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
135	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg0NzY3LCJleHAiOjE3NjE0MjA3Njd9.SYB69ZghC_hLU8FoDDEfju32Adn5F7worsrG3Iiixbpqv0Vvgvvrm2Y7UdZyGt2d	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg0NzY3LCJleHAiOjE3NjE2MzY3Njd9.KpE2DPxhm50wKUwWTw9blpfUj4Uz5r6_3sCGaJLrHelmhG2deb8V_snrUfjDnbL0	4c88cc0e-b5f8-478c-928b-08cc12f38423
137	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg1MDQ4LCJleHAiOjE3NjE0MjEwNDh9.pEFvUFcVRxDUE_t4DQODqFsnmuFIv8EOHene3EHsjg22Jb163S-WQPQrdihx1b77	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxMzg1MDQ4LCJleHAiOjE3NjE2MzcwNDh9.2kb24aT7YH8jNEuXZ9yXphN-q_MQ6n1g5QyXR0O6oaAS50VYV5Tg3tYoOieBxGFm	4c88cc0e-b5f8-478c-928b-08cc12f38423
138	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAwNDQ5LCJleHAiOjE3NjE0MzY0NDl9._CSTxb-QDFdddIzhTRbogajOKh9ZjquXrISsYucvoRyMPOfuFS8QtzLDOSdbaaif	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAwNDQ5LCJleHAiOjE3NjE2NTI0NDl9.wue-GYiqtYtDocQidZTswDVyYbEUmpjiq3pEBzrVjGxBc7gjtWiUbgQL2N5CjcIS	4c88cc0e-b5f8-478c-928b-08cc12f38423
139	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAwODkxLCJleHAiOjE3NjE0MzY4OTF9.RskZ54d_fWn_cRpd-SXRpJJetwGkyG-4jC0qxEXIDveP5gAbjJW78CCfRK6-7DsM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAwODkxLCJleHAiOjE3NjE2NTI4OTF9.9kNRqaIj9nXniMxlSXCL9-P935YMd0APO4wwIxSNVNqW7sOAmuutPibfkKT1NlZx	4c88cc0e-b5f8-478c-928b-08cc12f38423
136	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTM4NDc4NiwiZXhwIjoxNzYxNDIwNzg2fQ.6nqUQQD2LpnFVTUdxozh-QYOfzMMrsnde4VCC5CFeWRreRZxAhTCPQQUYZm2OOVx	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTM4NDc4NiwiZXhwIjoxNzYxNjM2Nzg2fQ.eNqWKJry0GHcN_XTE6yPgyFo8OMZVIoe9DLOInWVQq0zRR04b4TeXvhIsDNSnFJq	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
140	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAxMTM2LCJleHAiOjE3NjE0MzcxMzZ9.mjeV_XKAdsRqUDnHGpxJHgs3YEsXrLpK6bJ7Gx6-mtBAKh7ROgFzH6koDPto9Wpq	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDAxMTM2LCJleHAiOjE3NjE2NTMxMzZ9.dP9N0KTKsYkTe-5uREmpVXNdEPN3UayjJKiasjIklG8DF2cKWzeW1YjjolkCrq2N	4c88cc0e-b5f8-478c-928b-08cc12f38423
142	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDEwNTYxLCJleHAiOjE3NjE0NDY1NjF9.p12w-gZkRwqbgKBx66rVjtis_rPF5fCZPAC0J_hIT1oz8_8M1rQLhRall6LxJVBA	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDEwNTYxLCJleHAiOjE3NjE2NjI1NjF9.eKSCpVvGQHVzdA55ECgkx38CG7NOqSNJA2bS55XHmJoG0o6JZYgOocbzj-B5vD3L	4c88cc0e-b5f8-478c-928b-08cc12f38423
141	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQxMDU0OSwiZXhwIjoxNzYxNDQ2NTQ5fQ.2TyCaIiIYKMsCadFq944XYmqE46F9aaqdsq87kVneXtzjSN-URC72dYlvWBAweVk	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQxMDU0OSwiZXhwIjoxNzYxNjYyNTQ5fQ.fsK3669UBbyuAzn2LKNJSf9XWKcWU8XfdJlzfossobuji8x2bZ30RIauvq4C5_ZB	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
144	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ3MDg2MSwiZXhwIjoxNzYxNTA2ODYxfQ.CFxqOdsJ4aKsEGbejB7xg4QcR0_mMfbYbbKyjN8Jgb2k0QvEBd4voFK8fMwqGwqa	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ3MDg2MSwiZXhwIjoxNzYxNzIyODYxfQ.ksRZR52FdSXxnf22x-R5l9xoIm94Meek-g3EN3nbgXiBm4QEdZvWsVYho2g0rPCH	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
145	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDQ3MSwiZXhwIjoxNzYxNTIwNDcxfQ.AkI7iUPIUHSKwR3kDu2wO2U7a9r2eMIPTRzU-SkpsEaKKDebA71hDROgPahn9xTH	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDQ3MSwiZXhwIjoxNzYxNzM2NDcxfQ.A-iIxpRo2l5PQlq_HhtjS9qMtpM2uNEUxQnGQziw4sUF0WQm2KKPK4T4vReWvUOF	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
146	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDcwMCwiZXhwIjoxNzYxNTIwNzAwfQ.UfHfE3ZLtC4dnwY81XL-DJkkfO5qM4aX5FaxcKP8QOgmMnj38NCnwzsIUUQDrUBx	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDcwMCwiZXhwIjoxNzYxNzM2NzAwfQ.qjVkh-oTogE7aAYoOILqiuqInUARzutMdrj_Y6lA3UvxBTgj-1VqrXiReJl1AnC5	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
147	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDcyNiwiZXhwIjoxNzYxNTIwNzI2fQ.8tuNQPpVpMoSutZRflOWuJ6Arlp_VG2SOeX-p63vPUbAdURWjaOAXwJp5iYU2ACk	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDcyNiwiZXhwIjoxNzYxNzM2NzI2fQ.-rCYJHbYE3m45G10PnQzsjLEsarljU7ki7DXTZBFmkCGzAdrN0GG_ofDtOHShgAC	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
148	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDgyNSwiZXhwIjoxNzYxNTIwODI1fQ.2M3wbHA8ZOyLjfmETnViyYZkW9CqVElwXzehyrKlCbPkpVNnmxoiq8x3iSSTWKm1	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDgyNSwiZXhwIjoxNzYxNzM2ODI1fQ.wPP4fYVyLYZMO6MQ92g6TFHXKZmljYypXo756-yhGovH8NV4zsdrbpRoIpcgejab	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
149	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDgzNCwiZXhwIjoxNzYxNTIwODM0fQ.wuZhQhguJQu54sKrRoVyaYspg2dYymfY24IPa_ckXMOI1KpR8oPzudkPInMtA5jn	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTQ4NDgzNCwiZXhwIjoxNzYxNzM2ODM0fQ.hvN2nKmIzzDxVGxVuZWWutJxFvpU0vcLHHDiKrh3U4r3PFsVosL92qPyKKUt328J	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
143	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDEzNTI0LCJleHAiOjE3NjE0NDk1MjR9.QuE86AzmLWZ-xTet0GmZKvTXVyVSgA97LSt9fAgv5OpnlXbwJNH9o1b_vwwVwwFY	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNDEzNTI0LCJleHAiOjE3NjE2NjU1MjR9.46DqrXDB-kz4AQ0FeoZLylrA_GfqU-LGjVKpMcw9Q4_3Jjhxvq5jrfUyLURSfwVM	4c88cc0e-b5f8-478c-928b-08cc12f38423
151	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTU5OTM3LCJleHAiOjE3NjE1OTU5Mzd9.sQzAR1mJXjSSFYBBMqArf3cWKcLd8u-3RW9Vst5VMwlupxqSDO1kevfzU1VaJBpk	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTU5OTM3LCJleHAiOjE3NjE4MTE5Mzd9.kik-POngsGOCW7Egg28mkmvA1Ux9fefGM3jDZr4u8G135ZhgAshH0eXJ9FJ6ulOH	4c88cc0e-b5f8-478c-928b-08cc12f38423
152	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTYwMDc3LCJleHAiOjE3NjE1OTYwNzd9.xQCpi-AbAFY9eGG9gra3WjF0HHC4KiFZhh89wOJcl9bH0yT_oC1WWIBcwXeawmKX	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTYwMDc3LCJleHAiOjE3NjE4MTIwNzd9.jl_1Uznccg1eWE6u1mlccgCNNEcfObaacwpYpqLnXJ30bWkx3SN4-RM7GP9Yz-Ej	4c88cc0e-b5f8-478c-928b-08cc12f38423
150	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU1Nzk2NywiZXhwIjoxNzYxNTkzOTY3fQ.f25K1RndQwT5bKCJeC_bCmlU5aFDk6lBxbLaY0wNC8sSdMULgEMXh6WMN8uNYuB8	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU1Nzk2NywiZXhwIjoxNzYxODA5OTY3fQ.K6-12FpM6RJxNZP2ND6YxKxHs-N0hXjYMcikobY0GDfJ-y0WXj9z98H1-l6fWOtW	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
153	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU2MDA4NywiZXhwIjoxNzYxNTk2MDg3fQ.pvMBn9oAvOtiW3cQfKWGpiEmk2JPDlEsXMKZLItI0_8zUaCZGJXsfvqMVIEaUIBh	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU2MDA4NywiZXhwIjoxNzYxODEyMDg3fQ.aUj7c686wMVx9w2PARFN_yqjdRpb1_XsySZN4tr2aRVn89I5ER6-3rYn4bDNw3TP	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
154	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTYxNDE3LCJleHAiOjE3NjE1OTc0MTd9.f60AzUum7CBCJdMdvEgJNBehJJKRCMesVy4ev1mijAq3uXi5X4a7cU8IGZQr8_Kg	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTYxNDE3LCJleHAiOjE3NjE4MTM0MTd9.Xfo9MiH89j0z-K07jEB8jLCuJ4H0eaUYrtDUbeDxp90pUyZHV-KSU1tknuk5vdYo	4c88cc0e-b5f8-478c-928b-08cc12f38423
155	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU3NTIwNSwiZXhwIjoxNzYxNjExMjA1fQ.M-UVhDsiekiw8JVx9_m7d5pp7-fZR8_FRcd9o6EXyWcENLqo-pj46BGA4rEKwILj	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU3NTIwNSwiZXhwIjoxNzYxODI3MjA1fQ.3kEgUmD_mp29B9__1Xi54GIPE9Dp-AxDk_Os0AhgZWJgP6nMm6b9itl8hz1LGbL1	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
156	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3NzI2LCJleHAiOjE3NjE2MjM3MjZ9.Gf7psVWHP56AEQwyYueyLWtZ0K1AsBfH6pyUh6X4Q100wPQwO8WF0QwaE-0lscfK	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3NzI2LCJleHAiOjE3NjE4Mzk3MjZ9.7E3wWdv3ICc3SVUZpJBEXfzVPSKMImZtNfIOwFFw6r5PpGIm8RKMjeZJOld8fOyH	4c88cc0e-b5f8-478c-928b-08cc12f38423
158	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3Nzc4LCJleHAiOjE3NjE2MjM3Nzh9.s0AhnVdMB5zPWptyhjLczD0XCVvLC-W72q4F9OGKWsQQqbtiIrOZ1yYvJj84xfhQ	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3Nzc4LCJleHAiOjE3NjE4Mzk3Nzh9.ltSu7Kz1vhhyU-CNlq5BSI8Y19RKYBZHvB3st9cMl49pge5oL3MOyPRgCo3a1hpc	4c88cc0e-b5f8-478c-928b-08cc12f38423
157	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU4NzczNywiZXhwIjoxNzYxNjIzNzM3fQ.yAQoKFK2qGbq8Z9d_nHk-udAcr_MJt9smPNjuCmk69dcCSvkT7UUs9gNB265SQQ3	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU4NzczNywiZXhwIjoxNzYxODM5NzM3fQ.yZX7-lNU5oQvOcpvMuHylfKrUKq6okWd0H2s_JiLtsgpyx94rdW3OHv5UHDNm9sF	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
159	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3NzkxLCJleHAiOjE3NjE2MjM3OTF9.wr2mLctcBTUgxf99KXzKUJTb7CaNlM1VJyRGsM--j_jbdzTQWVzOxymdFNOdrNyD	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTg3NzkxLCJleHAiOjE3NjE4Mzk3OTF9.3zOtBQAYYHM1Aj6cQZaKDwHv1XhHw2GashJW71HNfKOvHmg67B8MuiOY2CBpHE83	4c88cc0e-b5f8-478c-928b-08cc12f38423
161	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTkzNzY2LCJleHAiOjE3NjE2Mjk3NjZ9.DC3IldKPcinEGvrxqhK2LONxE-uH9p-Z2nnoQrmkOYqsXTAT-xTfEyLkZqVu-ECg	f	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzYxNTkzNzY2LCJleHAiOjE3NjE4NDU3NjZ9.Z0kIRjstSbqHlcqBM27Or1lua7esCiLaOV1DhRBZWi-g6t3f7pnus6lGMaTmWXxx	4c88cc0e-b5f8-478c-928b-08cc12f38423
160	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU5MzczMSwiZXhwIjoxNzYxNjI5NzMxfQ.dU-feDQuOLP_zf3aZ-O82RGkrMVt13ps0w3zLsCEENe9o4qelZpPvnKqcATiDWBe	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MTU5MzczMSwiZXhwIjoxNzYxODQ1NzMxfQ.J-yDf3-0VUVoDOD4-1duHyYzMrSTTiRrVqIkT7vLyZ_MCk2T5dhId33xUz25VIBc	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
162	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MjY5MzU0MCwiZXhwIjoxNzYyNzI5NTQwfQ.y-hYIzIyEIr4TjAVA6CXkmYH-2u2RAjuWdeAayYUUq4J2_U7i6n-KlR6eMkk-2F-	f	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc2MjY5MzU0MCwiZXhwIjoxNzYyOTQ1NTQwfQ.8AuYIe40F_1sJIW4Law3xMlHJizOeYUYh91RlA2LAj0qHubdM2WTJQfhWIBz1r9n	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
\.


--
-- TOC entry 5918 (class 0 OID 47949)
-- Dependencies: 239
-- Data for Name: users; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.users (id, email, password, role, username) FROM stdin;
2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	admintest@gmail.com	$2a$10$nbN9G5vpNbKMHxa3UBV6neyti/5HSci252r6uV2q8YbfwjlYerMlW	ADMIN	admin
76e28d58-f310-4341-9708-66d655db04f9	bilbo@gmail.com	$2a$10$4LuAfpfhrxcRl75ABRkSEOCVl0Y4GAF1KopGfiZ4fr14B0J0GT5sK	USER	bilbo
4c88cc0e-b5f8-478c-928b-08cc12f38423	test@gmail.com	$2a$10$DRY0d.OPcG9YMxWtr/qe8.M/h41pr3LLaJqKB8lMOC6KZjdXz8CrO	USER	test
84814195-edae-4695-804e-405f6ebaff5a	new_admin@gmail.com	$2a$10$BQcpGgLFPVRkvOr6wTqKj.QTBuwmTvfCBteIDteKHDGiO5/ywAg9i	ADMIN	new_admin
9135b811-a378-4345-898a-909ff2350d5e	r@gmail.com	$2a$10$izqTQAyVwr1GxadP0ZmQoeOXaJNUxDW68GqjqzjMn34EA9DzTc97y	ADMIN	r
bb89918f-3444-4d6b-a254-418193f39d87	n@gmail.com	$2a$10$O3tbXIY57jRatMG5SMW7NOo3/ieIPuGuWKbTAJIznTcVfBC5Pshk2	ADMIN	bd
d31a3292-d192-4e4a-9d3c-3cb1282ef01f	gg@nh	$2a$10$FtEeBkqCbgGFvvPmpZD69u1ULvUmPSImtDS0yN4Q6PbwViQpdbxs.	ADMIN	gg
\.


--
-- TOC entry 5919 (class 0 OID 47955)
-- Dependencies: 240
-- Data for Name: verifications; Type: TABLE DATA; Schema: geo_score_schema; Owner: postgres
--

COPY geo_score_schema.verifications (id, comment, create_at, evidence_photo_id, feature_id, is_official, location_id, organization_id, status, verified_id) FROM stdin;
\.


--
-- TOC entry 5705 (class 0 OID 47058)
-- Dependencies: 222
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- TOC entry 5928 (class 0 OID 0)
-- Dependencies: 231
-- Name: location_pending_copies_id_seq; Type: SEQUENCE SET; Schema: geo_score_schema; Owner: postgres
--

SELECT pg_catalog.setval('geo_score_schema.location_pending_copies_id_seq', 22, true);


--
-- TOC entry 5929 (class 0 OID 0)
-- Dependencies: 238
-- Name: token_table_id_seq; Type: SEQUENCE SET; Schema: geo_score_schema; Owner: postgres
--

SELECT pg_catalog.setval('geo_score_schema.token_table_id_seq', 162, true);


--
-- TOC entry 5720 (class 2606 OID 47961)
-- Name: barrierless_criteria_check barrierless_criteria_check_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_check
    ADD CONSTRAINT barrierless_criteria_check_pkey PRIMARY KEY (barrierless_criteria_id, location_id, user_id);


--
-- TOC entry 5722 (class 2606 OID 47963)
-- Name: barrierless_criteria_groups barrierless_criteria_groups_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_groups
    ADD CONSTRAINT barrierless_criteria_groups_pkey PRIMARY KEY (id);


--
-- TOC entry 5718 (class 2606 OID 47965)
-- Name: barrierless_criteria barrierless_criteria_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria
    ADD CONSTRAINT barrierless_criteria_pkey PRIMARY KEY (id);


--
-- TOC entry 5724 (class 2606 OID 47967)
-- Name: barrierless_criteria_types barrierless_criteria_types_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_types
    ADD CONSTRAINT barrierless_criteria_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5726 (class 2606 OID 47969)
-- Name: location_pending_copies location_pending_copies_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.location_pending_copies
    ADD CONSTRAINT location_pending_copies_pkey PRIMARY KEY (id);


--
-- TOC entry 5728 (class 2606 OID 47971)
-- Name: location_score_chg location_score_chg_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.location_score_chg
    ADD CONSTRAINT location_score_chg_pkey PRIMARY KEY (location_id);


--
-- TOC entry 5730 (class 2606 OID 47973)
-- Name: location_types location_types_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.location_types
    ADD CONSTRAINT location_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5734 (class 2606 OID 47975)
-- Name: locations locations_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5736 (class 2606 OID 47977)
-- Name: photos photos_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.photos
    ADD CONSTRAINT photos_pkey PRIMARY KEY (id);


--
-- TOC entry 5738 (class 2606 OID 47979)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 5741 (class 2606 OID 47981)
-- Name: token_table token_table_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.token_table
    ADD CONSTRAINT token_table_pkey PRIMARY KEY (id);


--
-- TOC entry 5743 (class 2606 OID 47983)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 5745 (class 2606 OID 47985)
-- Name: verifications verifications_pkey; Type: CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.verifications
    ADD CONSTRAINT verifications_pkey PRIMARY KEY (id);


--
-- TOC entry 5739 (class 1259 OID 47986)
-- Name: fki_fkbwajii9i2r8viibrhi88b8j4p; Type: INDEX; Schema: geo_score_schema; Owner: postgres
--

CREATE INDEX fki_fkbwajii9i2r8viibrhi88b8j4p ON geo_score_schema.token_table USING btree (user_id);


--
-- TOC entry 5731 (class 1259 OID 47987)
-- Name: idx_locations_address_trgm; Type: INDEX; Schema: geo_score_schema; Owner: postgres
--

CREATE INDEX idx_locations_address_trgm ON geo_score_schema.locations USING gin (address public.gin_trgm_ops);


--
-- TOC entry 5732 (class 1259 OID 47988)
-- Name: idx_locations_name_trgm; Type: INDEX; Schema: geo_score_schema; Owner: postgres
--

CREATE INDEX idx_locations_name_trgm ON geo_score_schema.locations USING gin (name public.gin_trgm_ops);


--
-- TOC entry 5747 (class 2606 OID 47989)
-- Name: barrierless_criteria_check fk1qyogdrj68at9d68ij80w3cc9; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_check
    ADD CONSTRAINT fk1qyogdrj68at9d68ij80w3cc9 FOREIGN KEY (location_id) REFERENCES geo_score_schema.locations(id);


--
-- TOC entry 5752 (class 2606 OID 47994)
-- Name: location_types fk1xbd4l000fic1q9l57njllk2h; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.location_types
    ADD CONSTRAINT fk1xbd4l000fic1q9l57njllk2h FOREIGN KEY (barrierless_criteria_group_id) REFERENCES geo_score_schema.barrierless_criteria_groups(id);


--
-- TOC entry 5748 (class 2606 OID 47999)
-- Name: barrierless_criteria_check fk6m2cpcr4jswtan1pvle5u67xy; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_check
    ADD CONSTRAINT fk6m2cpcr4jswtan1pvle5u67xy FOREIGN KEY (barrierless_criteria_id) REFERENCES geo_score_schema.barrierless_criteria(id);


--
-- TOC entry 5751 (class 2606 OID 48004)
-- Name: location_pending_copies fkawbr0dn98lctyhs9u63wdrha3; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.location_pending_copies
    ADD CONSTRAINT fkawbr0dn98lctyhs9u63wdrha3 FOREIGN KEY (location_id) REFERENCES geo_score_schema.locations(id);


--
-- TOC entry 5754 (class 2606 OID 48009)
-- Name: token_table fkbwajii9i2r8viibrhi88b8j4p; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.token_table
    ADD CONSTRAINT fkbwajii9i2r8viibrhi88b8j4p FOREIGN KEY (user_id) REFERENCES geo_score_schema.users(id);


--
-- TOC entry 5749 (class 2606 OID 48014)
-- Name: barrierless_criteria_check fkp6bb3uiqxiamkxalj2b7sftft; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_check
    ADD CONSTRAINT fkp6bb3uiqxiamkxalj2b7sftft FOREIGN KEY (user_id) REFERENCES geo_score_schema.users(id);


--
-- TOC entry 5746 (class 2606 OID 48019)
-- Name: barrierless_criteria fkpqbm9h7e9tq1o7btwbb1i8qxn; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria
    ADD CONSTRAINT fkpqbm9h7e9tq1o7btwbb1i8qxn FOREIGN KEY (barrierless_criteria_type_id) REFERENCES geo_score_schema.barrierless_criteria_types(id);


--
-- TOC entry 5753 (class 2606 OID 48024)
-- Name: locations fkq8hx1pr0bsid33mn35qs4psuk; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.locations
    ADD CONSTRAINT fkq8hx1pr0bsid33mn35qs4psuk FOREIGN KEY (location_type_id) REFERENCES geo_score_schema.location_types(id);


--
-- TOC entry 5750 (class 2606 OID 48029)
-- Name: barrierless_criteria_types fkr1vuix53ijsxcvwtmxmy07rx; Type: FK CONSTRAINT; Schema: geo_score_schema; Owner: postgres
--

ALTER TABLE ONLY geo_score_schema.barrierless_criteria_types
    ADD CONSTRAINT fkr1vuix53ijsxcvwtmxmy07rx FOREIGN KEY (barrierless_criteria_group_id) REFERENCES geo_score_schema.barrierless_criteria_groups(id);


-- Completed on 2025-11-09 15:24:15

--
-- PostgreSQL database dump complete
--

\unrestrict kNIj013OlPc7hLWlFYnFOIVtAKXxqD509W167McCNaosYFHMeeYPTjhYZCeNCbb

