--
-- PostgreSQL database dump
--

\restrict ThhQEc92HL6CzmhqNEgHRJn1zQlsKH1sOHIgkDKxAfXLG3xGRiocIzCD6ThUB9n

-- Dumped from database version 17.4
-- Dumped by pg_dump version 17.6

-- Started on 2025-09-29 20:47:33

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
-- TOC entry 5874 (class 0 OID 0)
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
-- TOC entry 5875 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- TOC entry 1680 (class 1247 OID 18783)
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
-- TOC entry 1683 (class 1247 OID 18804)
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
-- TOC entry 1686 (class 1247 OID 18814)
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
-- TOC entry 1689 (class 1247 OID 18832)
-- Name: moderation_status_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.moderation_status_enum AS ENUM (
    'pending',
    'approved',
    'rejected'
);


ALTER TYPE public.moderation_status_enum OWNER TO postgres;

--
-- TOC entry 1692 (class 1247 OID 18840)
-- Name: organization_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.organization_type_enum AS ENUM (
    'government',
    'business',
    'ngo'
);


ALTER TYPE public.organization_type_enum OWNER TO postgres;

--
-- TOC entry 1695 (class 1247 OID 18848)
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
-- TOC entry 235 (class 1259 OID 19009)
-- Name: barrierless_criteria_check; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.barrierless_criteria_check (
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


ALTER TABLE public.barrierless_criteria_check OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 18864)
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
-- TOC entry 226 (class 1259 OID 18869)
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
-- TOC entry 237 (class 1259 OID 19030)
-- Name: location_pending_copies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_pending_copies (
    id bigint NOT NULL,
    address character varying(500) NOT NULL,
    contacts jsonb,
    description text,
    name character varying(255) NOT NULL,
    organization_id uuid,
    status character varying(255) NOT NULL,
    updated_at timestamp(6) without time zone NOT NULL,
    working_hours jsonb,
    location_id uuid NOT NULL,
    CONSTRAINT location_pending_copies_status_check CHECK (((status)::text = ANY ((ARRAY['pending'::character varying, 'published'::character varying, 'rejected'::character varying])::text[])))
);


ALTER TABLE public.location_pending_copies OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 19029)
-- Name: location_pending_copies_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.location_pending_copies ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.location_pending_copies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 238 (class 1259 OID 19043)
-- Name: location_score_chg; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_score_chg (
    location_id uuid NOT NULL
);


ALTER TABLE public.location_score_chg OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 18874)
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
-- TOC entry 228 (class 1259 OID 18879)
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
-- TOC entry 229 (class 1259 OID 18885)
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
-- TOC entry 230 (class 1259 OID 18892)
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
-- TOC entry 234 (class 1259 OID 18985)
-- Name: token_table; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token_table (
    id bigint NOT NULL,
    access_token character varying(255),
    is_logged_out boolean,
    refresh_token character varying(255),
    user_id uuid
);


ALTER TABLE public.token_table OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 18984)
-- Name: token_table_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.token_table ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.token_table_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 231 (class 1259 OID 18899)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    email character varying(255),
    password character varying(255),
    role character varying(255),
    username character varying(255),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['USER'::character varying, 'ADMIN'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 18902)
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
-- TOC entry 5854 (class 0 OID 18855)
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
293c553a-533b-4ff4-b8ec-5f2b24735fd3	2025-09-21 21:36:42.613149	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	nice tests	\N	2025-09-21 21:36:42.613149	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
c3bf90e1-c8bf-4c2c-a86d-0a13d234facd	2025-09-21 21:46:09.607691	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	nice tests2	\N	2025-09-21 21:46:09.607691	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
e5c016c7-560a-4833-a022-420f1a15d4b1	2025-09-22 09:21:28.328096	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	nice tests3	\N	2025-09-22 09:21:28.328096	206ff3f5-f01e-49ad-ac7e-d4f5f35050d5	high
\.


--
-- TOC entry 5865 (class 0 OID 19009)
-- Dependencies: 235
-- Data for Name: barrierless_criteria_check; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.barrierless_criteria_check (barrier_free_rating, comment, created_at, created_by, has_issue, updated_at, barrierless_criteria_id, location_id, user_id) FROM stdin;
4.5	nice tests2	2025-09-26 19:37:16.49891	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	t	2025-09-26 19:37:16.49891	1f963a13-3602-4897-9c00-1ac8a31a6544	68fed16f-0cfa-400d-9d96-c4ee7f37b036	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
\.


--
-- TOC entry 5855 (class 0 OID 18864)
-- Dependencies: 225
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
-- TOC entry 5856 (class 0 OID 18869)
-- Dependencies: 226
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
-- TOC entry 5867 (class 0 OID 19030)
-- Dependencies: 237
-- Data for Name: location_pending_copies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location_pending_copies (id, address, contacts, description, name, organization_id, status, updated_at, working_hours, location_id) FROM stdin;
\.


--
-- TOC entry 5868 (class 0 OID 19043)
-- Dependencies: 238
-- Data for Name: location_score_chg; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.location_score_chg (location_id) FROM stdin;
\.


--
-- TOC entry 5857 (class 0 OID 18874)
-- Dependencies: 227
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
-- TOC entry 5858 (class 0 OID 18879)
-- Dependencies: 228
-- Data for Name: locations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.locations (id, address, contacts, coordinates, created_at, created_by, description, last_verified_at, name, organization_id, overall_accessibility_score, rejection_reason, status, updated_at, working_hours, location_type_id) FROM stdin;
3892691c-92b7-4295-8532-2f901e66538a	п'ятдесятна 22	{"email": "a@gmail.com", "phone": "+38076392813", "website": "website.com"}	0101000020E610000000000000000052400000000000804940	2025-09-22 20:57:22.850431	c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555	desc1	2025-04-13 00:00:00	об'єктна шняга4	f07b1a9f-018c-454f-9e90-3520d4c1ac38	\N	\N	pending	2025-09-22 20:57:22.850431	{"friday": {"open": "09:00", "close": "17:00"}, "monday": {"open": "09:00", "close": "18:00"}, "sunday": {"open": "09:00", "close": "17:00"}, "tuesday": {"open": "09:00", "close": "18:00"}, "saturday": {"open": "09:00", "close": "17:00"}, "thursday": {"open": "09:00", "close": "18:00"}, "wednesday": {"open": "09:00", "close": "18:00"}}	0ee443d4-213c-4746-97a8-fbc788252761
68fed16f-0cfa-400d-9d96-c4ee7f37b036	upd_address	{"email": "aupd@gmail.com", "phone": "+38076392813", "website": "websupdite.com"}	0101000020E610000000000000004053400000000000004940	2025-09-24 19:57:41.877438	c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555	upd_descr	2025-04-13 00:00:00	upd_yo	f07b1a9f-018c-454f-9e90-3520d4c1ac38	\N	\N	published	2025-09-28 11:40:28.934086	{"friday": {"open": "09:00", "close": "17:00"}, "monday": {"open": "09:00", "close": "18:00"}, "sunday": null, "tuesday": {"open": "09:00", "close": "18:00"}, "saturday": {"open": "09:00", "close": "17:00"}, "thursday": {"open": "09:00", "close": "18:00"}, "wednesday": {"open": "09:00", "close": "18:00"}}	0ee443d4-213c-4746-97a8-fbc788252761
312e9039-2ccf-48a1-a111-262b10a86c16	п'ятдесятна 22	{"email": "a@gmail.com", "phone": "+38076392813", "website": "website.com"}	0101000020E61000004521AF086D4D3F4095F84CEC25C04940	2025-09-22 20:49:50.723032	c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555	desc1	2025-04-13 00:00:00	об'єктна шняга3	f07b1a9f-018c-454f-9e90-3520d4c1ac38	\N	\N	pending	2025-09-22 20:49:50.723032	{"friday": {"open": "09:00", "close": "17:00"}, "monday": {"open": "09:00", "close": "18:00"}, "sunday": {"open": "09:00", "close": "17:00"}, "tuesday": {"open": "09:00", "close": "18:00"}, "saturday": {"open": "09:00", "close": "17:00"}, "thursday": {"open": "09:00", "close": "18:00"}, "wednesday": {"open": "09:00", "close": "18:00"}}	0ee443d4-213c-4746-97a8-fbc788252761
3c9c8637-3e51-438f-8601-7d8925b4ba6c	апдейтедп'ятдесятна 22	{"email": "aпдейт@gmail.com", "phone": "+38072392813", "website": "websiteапд.com"}	0101000020E61000005D6164FF81C2494017B4435D5D4D3F40	2025-09-29 20:37:16.821223	c4b22cb9-85cb-4e3d-b6c0-ff70cc98b555	desc1 апдейтед	2025-04-13 00:00:00	updated шняга	f07b1a9f-018c-454f-9e90-3520d4c1ac38	\N	\N	published	2025-09-29 20:37:16.821223	{"friday": {"open": "09:00", "close": "17:00"}, "monday": {"open": "09:00", "close": "18:00"}, "sunday": {"open": "09:00", "close": "17:00"}, "tuesday": {"open": "09:00", "close": "18:00"}, "saturday": {"open": "09:00", "close": "17:00"}, "thursday": {"open": "09:00", "close": "18:00"}, "wednesday": {"open": "09:00", "close": "18:00"}}	18fc4aba-410d-4768-99c8-f0902327ddae
\.


--
-- TOC entry 5859 (class 0 OID 18885)
-- Dependencies: 229
-- Data for Name: photos; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.photos (id, ai_accessibility_detection, ai_moderation_score, created_at, created_by, description, feature_id, location_id, metadata, reject_reason, thumbnail_url, url, moderation_status) FROM stdin;
\.


--
-- TOC entry 5860 (class 0 OID 18892)
-- Dependencies: 230
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reviews (id, accessibility_experience, comment, created_at, location_id, moderation_status, rating, updated_at, user_id) FROM stdin;
\.


--
-- TOC entry 5656 (class 0 OID 18024)
-- Dependencies: 220
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- TOC entry 5864 (class 0 OID 18985)
-- Dependencies: 234
-- Data for Name: token_table; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.token_table (id, access_token, is_logged_out, refresh_token, user_id) FROM stdin;
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
40	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5ODIzLCJleHAiOjE3NTg3NzU4MjN9.v_IaWxQi2c2ylfihW1D5kcvWYby9KjmS-wFr56k20Y0P4DaRe63XMV2E-0M9uko8	f	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNzU4NzM5ODIzLCJleHAiOjE3NTg5OTE4MjN9.wkg4Z53u3XEZjEIDDg1lJBgWdtTlGsGw6NjbOvp-RebDyEPD2k7kNIwdAek1bD-p	4c88cc0e-b5f8-478c-928b-08cc12f38423
36	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczODA5NSwiZXhwIjoxNzU4Nzc0MDk1fQ.DwxS6SM3sGbz680rVUoH5cWdO_KoLA0TOmvVlQN0i_XH2FTpnFnOO9xaJSseMuAe	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczODA5NSwiZXhwIjoxNzU4OTkwMDk1fQ.cc_HusLN2_ZwXlkBOk8kEbFHjw352jr2xS2rmsK3Rjkwt59dIpkPI6mSkAZlpOVN	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
41	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczOTg2MCwiZXhwIjoxNzU4Nzc1ODYwfQ.VtsTmSLujXDDPJ4fvdKEOtC3arld58zCarBQs03UxM2jU6PmmU_Tfv4eYRqReA7A	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODczOTg2MCwiZXhwIjoxNzU4OTkxODYwfQ.E1IoVyj_GsCcqmWNTMTpj-CwK_oN1wgN-yB13K3qpXxh1WN4nQipZru_c6ggQCHV	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
42	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODc0MDE1NywiZXhwIjoxNzU4Nzc2MTU3fQ.Ww0p7yDr1jGB0um41r3DLZpEoGt4VzuseAWGVruKwgNxwho3N5i6u92SDSCHqnTM	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1ODc0MDE1NywiZXhwIjoxNzU4OTkyMTU3fQ.uYL5X5Qz_DbimrqDJbEU-1ZfhDdetmFLK7qsZm8u1ycygwIr2ZDHItMKzUl8_F2q	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
43	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDcyNCwiZXhwIjoxNzU5MTIwNzI0fQ.nW7G76Ycc2Pm3W31MlFpWmhXwZ17r6IL3ClztDMq1xPGUwWY14Dkj6LVhvAF34Pb	t	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDcyNCwiZXhwIjoxNzU5MzM2NzI0fQ.JuZ85wJ0CEZY_k8Q0qGdbV0853SZl8F2MQCwC9Vi26IAS8KIdD5YEEG7pZYIWshf	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
44	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDgwNSwiZXhwIjoxNzU5MTIwODA1fQ.n9nZ7Mm9UoWgAxbq1lpJDE8u6cwilBYHZ9-nlgm7ku9KpIaWzz6tDTVOIfdLxB_8	f	eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTc1OTA4NDgwNSwiZXhwIjoxNzU5MzM2ODA1fQ.VfV9PLV2Ll0nXnUTHyJX1ompvUlw8cMFj1xacClRTfypgfORqKvRTehPRn9xwX8E	2fda00ee-d44a-49ef-bfdf-0b9e61f30f79
\.


--
-- TOC entry 5861 (class 0 OID 18899)
-- Dependencies: 231
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password, role, username) FROM stdin;
4c88cc0e-b5f8-478c-928b-08cc12f38423	test@gmail.com	$2a$10$DRY0d.OPcG9YMxWtr/qe8.M/h41pr3LLaJqKB8lMOC6KZjdXz8CrO	USER	test
2fda00ee-d44a-49ef-bfdf-0b9e61f30f79	admintest@gmail.com	$2a$10$nbN9G5vpNbKMHxa3UBV6neyti/5HSci252r6uV2q8YbfwjlYerMlW	ADMIN	admin
\.


--
-- TOC entry 5862 (class 0 OID 18902)
-- Dependencies: 232
-- Data for Name: verifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.verifications (id, comment, create_at, evidence_photo_id, feature_id, is_official, location_id, organization_id, status, verified_id) FROM stdin;
\.


--
-- TOC entry 5876 (class 0 OID 0)
-- Dependencies: 236
-- Name: location_pending_copies_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.location_pending_copies_id_seq', 1, true);


--
-- TOC entry 5877 (class 0 OID 0)
-- Dependencies: 233
-- Name: token_table_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.token_table_id_seq', 44, true);


--
-- TOC entry 5690 (class 2606 OID 19013)
-- Name: barrierless_criteria_check barrierless_criteria_check_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT barrierless_criteria_check_pkey PRIMARY KEY (barrierless_criteria_id, location_id, user_id);


--
-- TOC entry 5671 (class 2606 OID 18910)
-- Name: barrierless_criteria_groups barrierless_criteria_groups_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_groups
    ADD CONSTRAINT barrierless_criteria_groups_pkey PRIMARY KEY (id);


--
-- TOC entry 5669 (class 2606 OID 18912)
-- Name: barrierless_criteria barrierless_criteria_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria
    ADD CONSTRAINT barrierless_criteria_pkey PRIMARY KEY (id);


--
-- TOC entry 5673 (class 2606 OID 18914)
-- Name: barrierless_criteria_types barrierless_criteria_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_types
    ADD CONSTRAINT barrierless_criteria_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5692 (class 2606 OID 19037)
-- Name: location_pending_copies location_pending_copies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_pending_copies
    ADD CONSTRAINT location_pending_copies_pkey PRIMARY KEY (id);


--
-- TOC entry 5694 (class 2606 OID 19047)
-- Name: location_score_chg location_score_chg_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_score_chg
    ADD CONSTRAINT location_score_chg_pkey PRIMARY KEY (location_id);


--
-- TOC entry 5675 (class 2606 OID 18916)
-- Name: location_types location_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_types
    ADD CONSTRAINT location_types_pkey PRIMARY KEY (id);


--
-- TOC entry 5677 (class 2606 OID 18918)
-- Name: locations locations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (id);


--
-- TOC entry 5679 (class 2606 OID 18920)
-- Name: photos photos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.photos
    ADD CONSTRAINT photos_pkey PRIMARY KEY (id);


--
-- TOC entry 5681 (class 2606 OID 18922)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 5688 (class 2606 OID 18991)
-- Name: token_table token_table_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_table
    ADD CONSTRAINT token_table_pkey PRIMARY KEY (id);


--
-- TOC entry 5683 (class 2606 OID 18924)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 5685 (class 2606 OID 18926)
-- Name: verifications verifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.verifications
    ADD CONSTRAINT verifications_pkey PRIMARY KEY (id);


--
-- TOC entry 5686 (class 1259 OID 19008)
-- Name: fki_fkbwajii9i2r8viibrhi88b8j4p; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fkbwajii9i2r8viibrhi88b8j4p ON public.token_table USING btree (user_id);


--
-- TOC entry 5700 (class 2606 OID 19019)
-- Name: barrierless_criteria_check fk1qyogdrj68at9d68ij80w3cc9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fk1qyogdrj68at9d68ij80w3cc9 FOREIGN KEY (location_id) REFERENCES public.locations(id);


--
-- TOC entry 5697 (class 2606 OID 18932)
-- Name: location_types fk1xbd4l000fic1q9l57njllk2h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_types
    ADD CONSTRAINT fk1xbd4l000fic1q9l57njllk2h FOREIGN KEY (barrierless_criteria_group_id) REFERENCES public.barrierless_criteria_groups(id);


--
-- TOC entry 5701 (class 2606 OID 19014)
-- Name: barrierless_criteria_check fk6m2cpcr4jswtan1pvle5u67xy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fk6m2cpcr4jswtan1pvle5u67xy FOREIGN KEY (barrierless_criteria_id) REFERENCES public.barrierless_criteria(id);


--
-- TOC entry 5703 (class 2606 OID 19038)
-- Name: location_pending_copies fkawbr0dn98lctyhs9u63wdrha3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_pending_copies
    ADD CONSTRAINT fkawbr0dn98lctyhs9u63wdrha3 FOREIGN KEY (location_id) REFERENCES public.locations(id);


--
-- TOC entry 5699 (class 2606 OID 19003)
-- Name: token_table fkbwajii9i2r8viibrhi88b8j4p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token_table
    ADD CONSTRAINT fkbwajii9i2r8viibrhi88b8j4p FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 5702 (class 2606 OID 19024)
-- Name: barrierless_criteria_check fkp6bb3uiqxiamkxalj2b7sftft; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_check
    ADD CONSTRAINT fkp6bb3uiqxiamkxalj2b7sftft FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 5695 (class 2606 OID 18947)
-- Name: barrierless_criteria fkpqbm9h7e9tq1o7btwbb1i8qxn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria
    ADD CONSTRAINT fkpqbm9h7e9tq1o7btwbb1i8qxn FOREIGN KEY (barrierless_criteria_type_id) REFERENCES public.barrierless_criteria_types(id);


--
-- TOC entry 5698 (class 2606 OID 18952)
-- Name: locations fkq8hx1pr0bsid33mn35qs4psuk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.locations
    ADD CONSTRAINT fkq8hx1pr0bsid33mn35qs4psuk FOREIGN KEY (location_type_id) REFERENCES public.location_types(id);


--
-- TOC entry 5696 (class 2606 OID 18957)
-- Name: barrierless_criteria_types fkr1vuix53ijsxcvwtmxmy07rx; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.barrierless_criteria_types
    ADD CONSTRAINT fkr1vuix53ijsxcvwtmxmy07rx FOREIGN KEY (barrierless_criteria_group_id) REFERENCES public.barrierless_criteria_groups(id);


-- Completed on 2025-09-29 20:47:34

--
-- PostgreSQL database dump complete
--

\unrestrict ThhQEc92HL6CzmhqNEgHRJn1zQlsKH1sOHIgkDKxAfXLG3xGRiocIzCD6ThUB9n

