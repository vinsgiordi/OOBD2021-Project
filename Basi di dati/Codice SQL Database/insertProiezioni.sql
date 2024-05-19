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
-- Data for Name: Proiezioni; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

INSERT INTO multisala."Proiezioni"	VALUES(80, 16:00:00, 18, 1,	12.00, "t", 24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(35, 09:30:00, 18, 1,	12.00, "t",	24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(36, 22:30:00, 6,	2, 8.50, "t", 24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(37, 08:30:00, 8, 3, 5.00, "t", 24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(67, 21:30:00, 18, 1, 12.00, "t", 24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(81, 09:30:00, 7, 2,	12.00, "t",	24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(82, 12:30:00, 7, 2,	12.00, "t",	24/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(85, 08:30:00, 8,	3,	12.00, "t",	25/02/2022);
INSERT INTO multisala."Proiezioni"	VALUES(86, 21:50:00, 26, 3,	8.50, "t", 03/03/2022);
INSERT INTO multisala."Proiezioni"	VALUES(88, 09:30:00, 6, 1,	9.00, "t", 13/03/2022);
INSERT INTO multisala."Proiezioni"	VALUES(90, 10:00:00, 26, 1,	8.00, "t", 13/03/2022);
INSERT INTO multisala."Proiezioni"	VALUES(91, 11:00:00, 26, 2,	10.00, "t", 13/03/2022);
INSERT INTO multisala."Proiezioni"	VALUES(92, 21:00:00, 28, 1, 8.00, "t", 21/03/2022);


-- TOC entry 3020 (class 0 OID 0)
-- Dependencies: 207
-- Name: Proiezioni_ID_Proiezione_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Proiezioni_ID_Proiezione_seq"', 93, true);

--
-- PostgreSQL database dump complete
--

