--
-- PostgreSQL database dump
--

\restrict hbIjBUe6MFmlIwkMCGLPtTnXw1oATZ0NSRtaxMXMRxvnqnwpCAWNG6dmXOGG8kZ

-- Dumped from database version 18.3
-- Dumped by pg_dump version 18.3

-- Started on 2026-10-07 10:28:14

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

DROP DATABASE IF EXISTS editoresymazmorras;
--
-- TOC entry 5045 (class 1262 OID 16460)
-- Name: editoresymazmorras; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE editoresymazmorras WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Spain.1252';


ALTER DATABASE editoresymazmorras OWNER TO postgres;

\unrestrict hbIjBUe6MFmlIwkMCGLPtTnXw1oATZ0NSRtaxMXMRxvnqnwpCAWNG6dmXOGG8kZ
\connect editoresymazmorras
\restrict hbIjBUe6MFmlIwkMCGLPtTnXw1oATZ0NSRtaxMXMRxvnqnwpCAWNG6dmXOGG8kZ

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
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- TOC entry 5046 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 225 (class 1259 OID 16507)
-- Name: cofre; Type: TABLE; Schema: public; Owner: minitienda
--

CREATE TABLE public.cofre (
    mazmorra bigint NOT NULL,
    posx integer NOT NULL,
    posy integer NOT NULL,
    planta integer NOT NULL,
    objetos character varying(64)[] NOT NULL
);


ALTER TABLE public.cofre OWNER TO minitienda;

--
-- TOC entry 224 (class 1259 OID 16506)
-- Name: cofre_mazmorra_seq; Type: SEQUENCE; Schema: public; Owner: minitienda
--

CREATE SEQUENCE public.cofre_mazmorra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cofre_mazmorra_seq OWNER TO minitienda;

--
-- TOC entry 5047 (class 0 OID 0)
-- Dependencies: 224
-- Name: cofre_mazmorra_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: minitienda
--

ALTER SEQUENCE public.cofre_mazmorra_seq OWNED BY public.cofre.mazmorra;


--
-- TOC entry 223 (class 1259 OID 16489)
-- Name: elementomazmorra; Type: TABLE; Schema: public; Owner: minitienda
--

CREATE TABLE public.elementomazmorra (
    mazmorra bigint NOT NULL,
    posx integer NOT NULL,
    posy integer NOT NULL,
    planta integer NOT NULL,
    tipo character varying(64) NOT NULL
);


ALTER TABLE public.elementomazmorra OWNER TO minitienda;

--
-- TOC entry 222 (class 1259 OID 16488)
-- Name: elementomazmorra_mazmorra_seq; Type: SEQUENCE; Schema: public; Owner: minitienda
--

CREATE SEQUENCE public.elementomazmorra_mazmorra_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.elementomazmorra_mazmorra_seq OWNER TO minitienda;

--
-- TOC entry 5048 (class 0 OID 0)
-- Dependencies: 222
-- Name: elementomazmorra_mazmorra_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: minitienda
--

ALTER SEQUENCE public.elementomazmorra_mazmorra_seq OWNED BY public.elementomazmorra.mazmorra;


--
-- TOC entry 221 (class 1259 OID 16469)
-- Name: mazmorra; Type: TABLE; Schema: public; Owner: minitienda
--

CREATE TABLE public.mazmorra (
    id bigint NOT NULL,
    nombre character varying(64) NOT NULL,
    upvotes bigint DEFAULT 0 NOT NULL,
    downvotes bigint DEFAULT 0 NOT NULL,
    creador uuid NOT NULL
);


ALTER TABLE public.mazmorra OWNER TO minitienda;

--
-- TOC entry 220 (class 1259 OID 16468)
-- Name: mazmorra_id_seq; Type: SEQUENCE; Schema: public; Owner: minitienda
--

CREATE SEQUENCE public.mazmorra_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.mazmorra_id_seq OWNER TO minitienda;

--
-- TOC entry 5049 (class 0 OID 0)
-- Dependencies: 220
-- Name: mazmorra_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: minitienda
--

ALTER SEQUENCE public.mazmorra_id_seq OWNED BY public.mazmorra.id;


--
-- TOC entry 219 (class 1259 OID 16461)
-- Name: usuario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.usuario (
    id uuid DEFAULT gen_random_uuid() NOT NULL,
    nombre character varying(32) NOT NULL
);


ALTER TABLE public.usuario OWNER TO postgres;

--
-- TOC entry 4874 (class 2604 OID 16510)
-- Name: cofre mazmorra; Type: DEFAULT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.cofre ALTER COLUMN mazmorra SET DEFAULT nextval('public.cofre_mazmorra_seq'::regclass);


--
-- TOC entry 4873 (class 2604 OID 16492)
-- Name: elementomazmorra mazmorra; Type: DEFAULT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.elementomazmorra ALTER COLUMN mazmorra SET DEFAULT nextval('public.elementomazmorra_mazmorra_seq'::regclass);


--
-- TOC entry 4870 (class 2604 OID 16472)
-- Name: mazmorra id; Type: DEFAULT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.mazmorra ALTER COLUMN id SET DEFAULT nextval('public.mazmorra_id_seq'::regclass);


--
-- TOC entry 5039 (class 0 OID 16507)
-- Dependencies: 225
-- Data for Name: cofre; Type: TABLE DATA; Schema: public; Owner: minitienda
--



--
-- TOC entry 5037 (class 0 OID 16489)
-- Dependencies: 223
-- Data for Name: elementomazmorra; Type: TABLE DATA; Schema: public; Owner: minitienda
--



--
-- TOC entry 5035 (class 0 OID 16469)
-- Dependencies: 221
-- Data for Name: mazmorra; Type: TABLE DATA; Schema: public; Owner: minitienda
--



--
-- TOC entry 5033 (class 0 OID 16461)
-- Dependencies: 219
-- Data for Name: usuario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 5050 (class 0 OID 0)
-- Dependencies: 224
-- Name: cofre_mazmorra_seq; Type: SEQUENCE SET; Schema: public; Owner: minitienda
--

SELECT pg_catalog.setval('public.cofre_mazmorra_seq', 1, false);


--
-- TOC entry 5051 (class 0 OID 0)
-- Dependencies: 222
-- Name: elementomazmorra_mazmorra_seq; Type: SEQUENCE SET; Schema: public; Owner: minitienda
--

SELECT pg_catalog.setval('public.elementomazmorra_mazmorra_seq', 1, false);


--
-- TOC entry 5052 (class 0 OID 0)
-- Dependencies: 220
-- Name: mazmorra_id_seq; Type: SEQUENCE SET; Schema: public; Owner: minitienda
--

SELECT pg_catalog.setval('public.mazmorra_id_seq', 1, false);


--
-- TOC entry 4882 (class 2606 OID 16519)
-- Name: cofre cofre_pkey; Type: CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.cofre
    ADD CONSTRAINT cofre_pkey PRIMARY KEY (mazmorra, posx, posy, planta);


--
-- TOC entry 4880 (class 2606 OID 16521)
-- Name: elementomazmorra elementomazmorra_pkey; Type: CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.elementomazmorra
    ADD CONSTRAINT elementomazmorra_pkey PRIMARY KEY (mazmorra, posx, posy, planta);


--
-- TOC entry 4878 (class 2606 OID 16481)
-- Name: mazmorra mazmorra_pkey; Type: CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.mazmorra
    ADD CONSTRAINT mazmorra_pkey PRIMARY KEY (id);


--
-- TOC entry 4876 (class 2606 OID 16467)
-- Name: usuario usuario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (id);


--
-- TOC entry 4883 (class 2606 OID 16482)
-- Name: mazmorra creador; Type: FK CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.mazmorra
    ADD CONSTRAINT creador FOREIGN KEY (creador) REFERENCES public.usuario(id) NOT VALID;


--
-- TOC entry 4885 (class 2606 OID 16522)
-- Name: cofre elementomazmorra; Type: FK CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.cofre
    ADD CONSTRAINT elementomazmorra FOREIGN KEY (mazmorra, posx, posy, planta) REFERENCES public.elementomazmorra(mazmorra, posx, posy, planta) NOT VALID;


--
-- TOC entry 4884 (class 2606 OID 16501)
-- Name: elementomazmorra mazmorra; Type: FK CONSTRAINT; Schema: public; Owner: minitienda
--

ALTER TABLE ONLY public.elementomazmorra
    ADD CONSTRAINT mazmorra FOREIGN KEY (mazmorra) REFERENCES public.mazmorra(id) NOT VALID;


-- Completed on 2026-10-07 10:28:14

--
-- PostgreSQL database dump complete
--

\unrestrict hbIjBUe6MFmlIwkMCGLPtTnXw1oATZ0NSRtaxMXMRxvnqnwpCAWNG6dmXOGG8kZ

