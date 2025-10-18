--
-- PostgreSQL database dump
--

\restrict ZDW94zwQPkdEBKrePH77y7z06Paxv3e6T7vrJVRYivTqLOlboh1xDGLnk18k7Px

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-18 20:51:27

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
-- TOC entry 2 (class 3079 OID 17665)
-- Name: pgcrypto; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgcrypto WITH SCHEMA public;


--
-- TOC entry 5844 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION pgcrypto; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgcrypto IS 'cryptographic functions';


--
-- TOC entry 3 (class 3079 OID 17702)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 5845 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- TOC entry 1675 (class 1247 OID 18783)
-- Name: feature_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.feature_type_enum AS ENUM (
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


ALTER TYPE public.feature_type_enum OWNER TO postgres;

--
-- TOC entry 1678 (class 1247 OID 18804)
-- Name: location_status_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.location_status_enum AS ENUM (
    'draft',
    'pending',
    'published',
    'rejected'
);


ALTER TYPE public.location_status_enum OWNER TO postgres;

--
-- TOC entry 1681 (class 1247 OID 18814)
-- Name: location_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.location_type_enum AS ENUM (
    'government_building',
    'business',
    'healthcare',
    'education',
    'culture',
    'transport',
    'recreation',
    'other'
);


ALTER TYPE public.location_type_enum OWNER TO postgres;

--
-- TOC entry 1684 (class 1247 OID 18832)
-- Name: moderation_status_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.moderation_status_enum AS ENUM (
    'pending',
    'approved',
    'rejected'
);


ALTER TYPE public.moderation_status_enum OWNER TO postgres;

--
-- TOC entry 1687 (class 1247 OID 18840)
-- Name: organization_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.organization_type_enum AS ENUM (
    'government',
    'business',
    'ngo'
);


ALTER TYPE public.organization_type_enum OWNER TO postgres;

--
-- TOC entry 1690 (class 1247 OID 18848)
-- Name: verification_status_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.verification_status_enum AS ENUM (
    'unverified',
    'pending',
    'verified'
);


ALTER TYPE public.verification_status_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 224 (class 1259 OID 18855)
-- Name: barrierless_criteria; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barrierless_criteria (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text,
    name character varying(255),
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_type_id uuid NOT NULL,
    rank character varying(255),
    CONSTRAINT barrierless_criteria_rank_check CHECK (((rank)::text = ANY ((ARRAY['high'::character varying, 'moderate'::character varying, 'low'::character varying])::text[])))
);


ALTER TABLE public.barrierless_criteria OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18861)
-- Name: barrierless_criteria_check; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barrierless_criteria_check (
    barrier_free boolean NOT NULL,
    barrier_free_rating boolean NOT NULL,
    comment character varying(255),
    barrierless_criteria_id uuid NOT NULL,
    location_id uuid NOT NULL,
    user_id uuid NOT NULL,
    has_issue boolean NOT NULL
);


ALTER TABLE public.barrierless_criteria_check OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 18864)
-- Name: barrierless_criteria_groups; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barrierless_criteria_groups (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL
);


ALTER TABLE public.barrierless_criteria_groups OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 18869)
-- Name: barrierless_criteria_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barrierless_criteria_types (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_group_id uuid NOT NULL
);


ALTER TABLE public.barrierless_criteria_types OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 18874)
-- Name: location_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_types (
    id uuid NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text NOT NULL,
    name character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    barrierless_criteria_group_id uuid NOT NULL
);


ALTER TABLE public.location_types OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 18879)
-- Name: locations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.locations (
    id uuid NOT NULL,
    address character varying(500) NOT NULL,
    contacts jsonb,
    coordinates public.geometry(Point,4326) NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    created_by uuid NOT NULL,
    description text,
    last_verified_at timestamp(6) without time zone NOT NULL,
    name character varying(255) NOT NULL,
    organization_id uuid,
    overall_accessibility_score integer,
    rejection_reason text,
    status character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    working_hours jsonb,
    location_type_id uuid NOT NULL,
    CONSTRAINT locations_status_check CHECK (((status)::text = ANY (ARRAY[('draft'::character varying)::text, ('pending'::character varying)::text, ('published'::character varying)::text, ('rejected'::character varying)::text])))
);


ALTER TABLE public.locations OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 18885)
-- Name: photos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.photos (
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
    CONSTRAINT photos_moderation_status_check CHECK (((moderation_status)::text = ANY ((ARRAY['pending'::character varying, 'approved'::character varying, 'rejected'::character varying])::text[])))
);


ALTER TABLE public.photos OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 18892)
-- Name: reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reviews (
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


ALTER TABLE public.reviews OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 18899)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 18902)
-- Name: verifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.verifications (
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


ALTER TABLE public.verifications OWNER TO postgres;

--
-- TOC entry 5829 (class 0 OID 18855)
-- Dependencies: 224
-- Data for Name: barrierless_criteria; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barrierless_criteria (id, created_at, created_by, description, name, updated_at, barrierless_criteria_type_id, rank) FROM stdin;
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
\.


--
-- TOC entry 5830 (class 0 OID 18861)
-- Dependencies: 225
-- Data for Name: barrierless_criteria_check; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barrierless_criteria_check (barrier_free, barrier_free_rating, comment, barrierless_criteria_id, location_id, user_id, has_issue) FROM stdin;
\.


--
-- TOC entry 5831 (class 0 OID 18864)
-- Dependencies: 226
-- Data for Name: barrierless_criteria_groups; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barrierless_criteria_groups (id, created_at, created_by, description, name, updated_at) FROM stdin;
410b96cc-c20f-456d-b861-609fbb443ed4	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Об’єкти благоустрою (парки, сквери, майдани, дитячі та спортивні майданчики, рекреаційні зони та інші об’єкти благоустрою населених пунктів)	amenities	2025-04-12 11:25:13.505412
28d11523-5483-4a85-bfa1-4ae409a73531	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Зупинки громадського транспорту (автобусні, трамвайні, тролейбусні, інші види зупинок)	public_transport_stops	2025-04-12 11:25:13.505412
6e4fb18b-5ee2-42cf-9bf1-c18987962166	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Об’єкти транспортної інфраструктури (залізничні вокзали і станції, автовокзали та автостанції, порти, причали, аеропорти)	transport_Infrastructure	2025-04-12 11:25:13.505412
9e2955b3-8aa0-49d3-a0a3-ada1c7fae9f8	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Вулиці та дороги (вулиці, бульвари, проспекти, провулки, дороги між населеними пунктами)	streets_and_roads	2025-04-12 11:25:13.505412
fb2cbe05-83eb-4923-abe8-cee76ce969bc	2025-04-12 11:25:13.505412	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	Будівлі та споруди (об’єкти фізичного оточення — загальні будівлі, приміщення, споруди)	buildings_and_structures	2025-04-12 11:25:13.505412
\.


--
-- TOC entry 5832 (class 0 OID 18869)
-- Dependencies: 227
-- Data for Name: barrierless_criteria_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barrierless_criteria_types (id, created_at, created_by, description, name, updated_at, barrierless_criteria_group_id) FROM stdin;
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
-- TOC entry 5833 (class 0 OID 18874)
-- Dependencies: 228
-- Data for Name: location_types; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location_types (id, created_at, created_by, description, name, updated_at, barrierless_criteria_group_id) FROM stdin;
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
-- TOC entry 5834 (class 0 OID 18879)
-- Dependencies: 229
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.locations (id, address, contacts, coordinates, created_at, created_by, description, last_verified_at, name, organization_id, overall_accessibility_score, rejection_reason, status, updated_at, working_hours, location_type_id) FROM stdin;
\.


--
-- TOC entry 5835 (class 0 OID 18885)
-- Dependencies: 230
-- Data for Name: photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.photos (id, ai_accessibility_detection, ai_moderation_score, created_at, created_by, description, feature_id, location_id, metadata, reject_reason, thumbnail_url, url, moderation_status) FROM stdin;
\.


--
-- TOC entry 5836 (class 0 OID 18892)
-- Dependencies: 231
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reviews (id, accessibility_experience, comment, created_at, location_id, moderation_status, rating, updated_at, user_id) FROM stdin;
\.


--
-- TOC entry 5642 (class 0 OID 18024)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- TOC entry 5837 (class 0 OID 18899)
-- Dependencies: 232
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id) FROM stdin;
\.


--
-- TOC entry 5838 (class 0 OID 18902)
-- Dependencies: 233
-- Data for Name: verifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verifications (id, comment, create_at, evidence_photo_id, feature_id, is_official, location_id, organization_id, status, verified_id) FROM stdin;
\.


--
-- TOC entry 5655 (class 2606 OID 18908)
-- Name: barrierless_criteria_check barrierless_criteria_check_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT barrierless_criteria_check_pkey PRIMARY KEY (barrierless_criteria_id, location_id, user_id);


--
-- TOC entry 5657 (class 2606 OID 18910)
-- Name: barrierless_criteria_groups barrierless_criteria_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_groups
    ADD CONSTRAINT barrierless_criteria_groups_pkey PRIMARY KEY (id);


--
-- TOC entry 5653 (class 2606 OID 18912)
-- Name: barrierless_criteria barrierless_criteria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria
    ADD CONSTRAINT barrierless_criteria_pkey PRIMARY KEY (id);


--
-- TOC entry 5659 (class 2606 OID 18914)
-- Name: barrierless_criteria_types barrierless_criteria_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_types
    ADD CONSTRAINT barrierless_criteria_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5661 (class 2606 OID 18916)
-- Name: location_types location_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_types
    ADD CONSTRAINT location_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5663 (class 2606 OID 18918)
-- Name: locations locations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5665 (class 2606 OID 18920)
-- Name: photos photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.photos
    ADD CONSTRAINT photos_pkey PRIMARY KEY (id);


--
-- TOC entry 5667 (class 2606 OID 18922)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 5669 (class 2606 OID 18924)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 5671 (class 2606 OID 18926)
-- Name: verifications verifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verifications
    ADD CONSTRAINT verifications_pkey PRIMARY KEY (id);


--
-- TOC entry 5673 (class 2606 OID 18927)
-- Name: barrierless_criteria_check fk1qyogdrj68at9d68ij80w3cc9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fk1qyogdrj68at9d68ij80w3cc9 FOREIGN KEY (location_id) REFERENCES public.locations(id);


--
-- TOC entry 5677 (class 2606 OID 18932)
-- Name: location_types fk1xbd4l000fic1q9l57njllk2h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_types
    ADD CONSTRAINT fk1xbd4l000fic1q9l57njllk2h FOREIGN KEY (barrierless_criteria_group_id) REFERENCES public.barrierless_criteria_groups(id);


--
-- TOC entry 5674 (class 2606 OID 18937)
-- Name: barrierless_criteria_check fk6m2cpcr4jswtan1pvle5u67xy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fk6m2cpcr4jswtan1pvle5u67xy FOREIGN KEY (barrierless_criteria_id) REFERENCES public.barrierless_criteria(id);


--
-- TOC entry 5675 (class 2606 OID 18942)
-- Name: barrierless_criteria_check fkp6bb3uiqxiamkxalj2b7sftft; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fkp6bb3uiqxiamkxalj2b7sftft FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 5672 (class 2606 OID 18947)
-- Name: barrierless_criteria fkpqbm9h7e9tq1o7btwbb1i8qxn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria
    ADD CONSTRAINT fkpqbm9h7e9tq1o7btwbb1i8qxn FOREIGN KEY (barrierless_criteria_type_id) REFERENCES public.barrierless_criteria_types(id);


--
-- TOC entry 5678 (class 2606 OID 18952)
-- Name: locations fkq8hx1pr0bsid33mn35qs4psuk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT fkq8hx1pr0bsid33mn35qs4psuk FOREIGN KEY (location_type_id) REFERENCES public.location_types(id);


--
-- TOC entry 5676 (class 2606 OID 18957)
-- Name: barrierless_criteria_types fkr1vuix53ijsxcvwtmxmy07rx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_types
    ADD CONSTRAINT fkr1vuix53ijsxcvwtmxmy07rx FOREIGN KEY (barrierless_criteria_group_id) REFERENCES public.barrierless_criteria_groups(id);


-- Completed on 2025-09-18 20:51:28

--
-- PostgreSQL database dump complete
--

\unrestrict ZDW94zwQPkdEBKrePH77y7z06Paxv3e6T7vrJVRYivTqLOlboh1xDGLnk18k7Px

