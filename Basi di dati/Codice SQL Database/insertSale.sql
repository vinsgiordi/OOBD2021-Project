--
-- PostgreSQL database dump
--

-- Dumped from database version 13.3
-- Dumped by pg_dump version 13.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Data for Name: Sale; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

INSERT INTO multisala."Sale" VALUES(1, "Alberto Sordi", "IMAX Dolby");
INSERT INTO multisala."Sale" VALUES(2, "Vittorio Gassman", "Dolby")
INSERT INTO multisala."Sale" VALUES(3, "Raffaele Viviani", "IMAX")

--
-- TOC entry 3016 (class 0 OID 0)
-- Dependencies: 209
-- Name: Sale_ID_Sala_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Sale_ID_Sala_seq"', 3, true);

--
-- PostgreSQL database dump complete
--

