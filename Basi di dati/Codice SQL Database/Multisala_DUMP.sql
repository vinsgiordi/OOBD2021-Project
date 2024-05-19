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

--
-- TOC entry 5 (class 2615 OID 49218)
-- Name: multisala; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA multisala;


ALTER SCHEMA multisala OWNER TO postgres;

--
-- TOC entry 221 (class 1255 OID 49219)
-- Name: check_film_proiezioni(); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.check_film_proiezioni() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
v_idfilm INTEGER;
v_idsala INTEGER;
v_orainiziofilm TIME WITHOUT TIME ZONE;
v_orafinefilm TIME WITHOUT TIME ZONE;
v_day DATE;
v_duratafilm INTEGER;
BEGIN
	-- dichiarazione nuove variabili
	SELECT NEW."Cod_Film" INTO v_idfilm;
	SELECT NEW."Cod_Sala" INTO v_idsala;
	SELECT NEW."Ora_Inizio" INTO v_orainiziofilm;
	SELECT NEW."Day" INTO v_day;
	SELECT "Durata" INTO v_duratafilm FROM multisala."Film" WHERE "ID_Film" = v_idfilm;
	SELECT v_orainiziofilm + (v_duratafilm * interval '1 minute') INTO v_orafinefilm;
	-- Controllo nelle proiezioni se la sala è già occupata
	IF(v_orafinefilm < v_orainiziofilm)
	THEN
		SELECT '23:59' INTO v_orafinefilm;
	END IF;
	IF EXISTS (
		SELECT *
		FROM multisala."Proiezioni"
		WHERE "Proiezione" = true AND "Day" = v_day
		AND "Ora_Inizio" BETWEEN v_orainiziofilm AND v_orafinefilm
		AND "Cod_Sala" = v_idsala
	) THEN
		RAISE EXCEPTION 'Sala occupata in questo orario';
	ELSEIF EXISTS (
		SELECT *
		FROM multisala."Proiezioni"
		WHERE "Proiezione" = true AND "Day" = v_day
		AND "Ora_Inizio" BETWEEN v_orainiziofilm AND v_orafinefilm
		AND "Cod_Film" = v_idfilm
	) THEN
		RAISE EXCEPTION 'Film già in programmazione in altra sala';
	ELSE
		RETURN NEW;
	END IF;
END
$$;


ALTER FUNCTION multisala.check_film_proiezioni() OWNER TO postgres;

--
-- TOC entry 222 (class 1255 OID 49220)
-- Name: get_biglietti_disponibili(integer, date); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.get_biglietti_disponibili(id_proiezione integer, giorno date) RETURNS TABLE(id_posti integer, numero integer, lettera character varying, cod_sala integer, occupato integer)
    LANGUAGE sql
    AS $$
SELECT posti."ID_Posti", posti."Numero", posti."Lettera", posti."Cod_Sala",
CASE 
    WHEN biglietti."ID_Biglietto" is null
        THEN 0
		ELSE 1
END AS Occupato
FROM multisala."Posti" AS posti
LEFT JOIN multisala."Proiezioni" AS proiezioni ON proiezioni."Cod_Sala" = posti."Cod_Sala"
LEFT JOIN multisala."Biglietti" AS biglietti ON biglietti."Cod_Posto" = posti."ID_Posti" AND biglietti."Cod_Proiezione" = id_proiezione
WHERE proiezioni."ID_Proiezione" = id_proiezione AND proiezioni."Day" = giorno
ORDER BY posti."Lettera";
$$;


ALTER FUNCTION multisala.get_biglietti_disponibili(id_proiezione integer, giorno date) OWNER TO postgres;

--
-- TOC entry 223 (class 1255 OID 49221)
-- Name: get_film(); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.get_film() RETURNS TABLE(id_film integer, titolo text, durata integer, data_inizio date, data_fine date, attivo boolean, best_price integer)
    LANGUAGE sql
    AS $$
SELECT "ID_Film", "Titolo", "Durata", "Data_Inizio", "Data_Fine", "Attivo", 
CASE 
    WHEN costi."prezzo" is null
        THEN 0
		ELSE 1  
END AS best_price
FROM multisala."Film" 
LEFT JOIN (SELECT "codice_film", "prezzo" FROM multisala.spettacoli_remunerativi()) AS costi ON costi."codice_film" = "ID_Film"
WHERE "Attivo" = true
$$;


ALTER FUNCTION multisala.get_film() OWNER TO postgres;

--
-- TOC entry 224 (class 1255 OID 49222)
-- Name: get_new_biglietto(integer, integer, character); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.get_new_biglietto(id_proiezione integer, numero_posto integer, lettera_posto character) RETURNS integer
    LANGUAGE plpgsql
    AS $$
	DECLARE 
	v_sala_proiezione int;
	v_id_posto int;
	
	BEGIN
		SELECT "Cod_Sala" INTO v_sala_proiezione FROM multisala."Proiezioni" WHERE "ID_Proiezione" = id_proiezione;

		SELECT "ID_Posti" INTO v_id_posto FROM multisala."Posti" WHERE "Numero" = numero_posto AND "Lettera" = lettera_posto; 

		INSERT INTO multisala."Biglietti"("Cod_Posto", "Cod_Proiezione") VALUES (v_id_posto, v_sala_proiezione);
		
		RETURN LASTVAL();
	END
$$;


ALTER FUNCTION multisala.get_new_biglietto(id_proiezione integer, numero_posto integer, lettera_posto character) OWNER TO postgres;

--
-- TOC entry 225 (class 1255 OID 49223)
-- Name: get_proiezioni(date); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.get_proiezioni(giorno date) RETURNS TABLE(id_proiezione integer, ora_inizio time without time zone, film character varying, sala character varying, prezzo numeric, best_orario integer)
    LANGUAGE sql
    AS $$
SELECT MIN("ID_Proiezione"),"Ora_Inizio", "Titolo", "Nome", "Prezzo", 
CASE 
    WHEN orario."ora" is null
        THEN 0
		ELSE 1  
END AS Best_Orario
FROM multisala."Proiezioni" 
LEFT JOIN multisala."Film" ON "Cod_Film" = "ID_Film"
LEFT JOIN multisala."Sale" ON "Cod_Sala" = "ID_Sala"
LEFT JOIN (SELECT "numero", "ora" FROM multisala.orario_di_maggiore_affluenza()) AS orario ON orario."ora" = "Ora_Inizio"
WHERE "Proiezione" = true AND "Day" = giorno AND "Attivo" = true
GROUP BY "Ora_Inizio", "Titolo", "Nome", "Prezzo", orario."ora"
ORDER BY "Ora_Inizio", "Titolo", "Nome", "Prezzo"
$$;


ALTER FUNCTION multisala.get_proiezioni(giorno date) OWNER TO postgres;

--
-- TOC entry 226 (class 1255 OID 49224)
-- Name: orario_di_maggiore_affluenza(); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.orario_di_maggiore_affluenza() RETURNS TABLE(ora time without time zone, numero integer)
    LANGUAGE sql
    AS $$
SELECT "Ora_Inizio", COUNT(*)
FROM multisala."Biglietti" LEFT JOIN multisala."Proiezioni" ON "Cod_Proiezione" = "ID_Proiezione"
GROUP BY "Ora_Inizio"
ORDER BY COUNT(*);
$$;


ALTER FUNCTION multisala.orario_di_maggiore_affluenza() OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 49225)
-- Name: spettacoli_remunerativi(); Type: FUNCTION; Schema: multisala; Owner: postgres
--

CREATE FUNCTION multisala.spettacoli_remunerativi() RETURNS TABLE(codice_film integer, prezzo numeric)
    LANGUAGE sql
    AS $$
SELECT "Cod_Film", SUM("Prezzo")
FROM multisala."Biglietti" LEFT JOIN multisala."Proiezioni" ON "Cod_Proiezione" = "ID_Proiezione"
GROUP BY "Cod_Film";
$$;


ALTER FUNCTION multisala.spettacoli_remunerativi() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 49226)
-- Name: Biglietti; Type: TABLE; Schema: multisala; Owner: postgres
--

CREATE TABLE multisala."Biglietti" (
    "ID_Biglietto" integer NOT NULL,
    "Cod_Posto" integer NOT NULL,
    "Cod_Proiezione" integer NOT NULL
);


ALTER TABLE multisala."Biglietti" OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 49229)
-- Name: Biglietti_ID_Biglietto_seq; Type: SEQUENCE; Schema: multisala; Owner: postgres
--

ALTER TABLE multisala."Biglietti" ALTER COLUMN "ID_Biglietto" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME multisala."Biglietti_ID_Biglietto_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 202 (class 1259 OID 49231)
-- Name: Film; Type: TABLE; Schema: multisala; Owner: postgres
--

CREATE TABLE multisala."Film" (
    "ID_Film" integer NOT NULL,
    "Titolo" character varying(50) NOT NULL,
    "Durata" integer NOT NULL,
    "Data_Inizio" date NOT NULL,
    "Data_Fine" date,
    "Attivo" boolean
);


ALTER TABLE multisala."Film" OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 49234)
-- Name: Film_ID_Film_seq; Type: SEQUENCE; Schema: multisala; Owner: postgres
--

ALTER TABLE multisala."Film" ALTER COLUMN "ID_Film" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME multisala."Film_ID_Film_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 204 (class 1259 OID 49236)
-- Name: Posti; Type: TABLE; Schema: multisala; Owner: postgres
--

CREATE TABLE multisala."Posti" (
    "ID_Posti" integer NOT NULL,
    "Numero" integer NOT NULL,
    "Lettera" "char" NOT NULL,
    "Cod_Sala" integer
);


ALTER TABLE multisala."Posti" OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 49239)
-- Name: Posti_ID_Posti_seq; Type: SEQUENCE; Schema: multisala; Owner: postgres
--

ALTER TABLE multisala."Posti" ALTER COLUMN "ID_Posti" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME multisala."Posti_ID_Posti_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 206 (class 1259 OID 49241)
-- Name: Proiezioni; Type: TABLE; Schema: multisala; Owner: postgres
--

CREATE TABLE multisala."Proiezioni" (
    "ID_Proiezione" integer NOT NULL,
    "Ora_Inizio" time without time zone NOT NULL,
    "Cod_Film" integer NOT NULL,
    "Cod_Sala" integer NOT NULL,
    "Prezzo" numeric(5,2) NOT NULL,
    "Proiezione" boolean DEFAULT true NOT NULL,
    "Day" date
);


ALTER TABLE multisala."Proiezioni" OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 49245)
-- Name: Proiezioni_ID_Proiezione_seq; Type: SEQUENCE; Schema: multisala; Owner: postgres
--

ALTER TABLE multisala."Proiezioni" ALTER COLUMN "ID_Proiezione" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME multisala."Proiezioni_ID_Proiezione_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 208 (class 1259 OID 49247)
-- Name: Sale; Type: TABLE; Schema: multisala; Owner: postgres
--

CREATE TABLE multisala."Sale" (
    "ID_Sala" integer NOT NULL,
    "Nome" character varying(50) NOT NULL,
    "Tecnologia" character varying
);


ALTER TABLE multisala."Sale" OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 49253)
-- Name: Sale_ID_Sala_seq; Type: SEQUENCE; Schema: multisala; Owner: postgres
--

ALTER TABLE multisala."Sale" ALTER COLUMN "ID_Sala" ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME multisala."Sale_ID_Sala_seq"
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3029 (class 0 OID 49226)
-- Dependencies: 200
-- Data for Name: Biglietti; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

COPY multisala."Biglietti" ("ID_Biglietto", "Cod_Posto", "Cod_Proiezione") FROM stdin;
580	257	37
581	295	37
582	294	37
583	293	37
584	384	37
585	382	37
586	383	37
587	261	37
588	262	37
589	277	37
590	278	37
591	257	86
592	275	86
593	276	86
594	277	86
595	278	86
596	279	86
597	36	35
598	37	35
599	1	90
600	2	90
601	3	90
602	131	91
603	149	91
604	150	91
605	131	91
606	149	91
607	150	91
608	179	91
609	180	91
610	19	92
611	20	92
612	21	92
\.


--
-- TOC entry 3031 (class 0 OID 49231)
-- Dependencies: 202
-- Data for Name: Film; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

COPY multisala."Film" ("ID_Film", "Titolo", "Durata", "Data_Inizio", "Data_Fine", "Attivo") FROM stdin;
7	Matrix	136	2021-09-23	2022-10-23	t
18	IT - Il film	190	2022-01-02	2021-01-03	t
26	The Batman	180	2022-03-04	2022-03-17	t
6	American Gangstar	170	2021-09-23	2022-10-23	t
8	Koda, Fratello Orso	85	2021-09-23	2022-10-23	t
27	One Piece	100	2022-03-13	2022-03-28	t
28	Harry Potter	100	2022-03-22	2022-03-31	f
\.


--
-- TOC entry 3033 (class 0 OID 49236)
-- Dependencies: 204
-- Data for Name: Posti; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

COPY multisala."Posti" ("ID_Posti", "Numero", "Lettera", "Cod_Sala") FROM stdin;
1	1	A	1
2	2	A	1
3	3	A	1
4	4	A	1
5	5	A	1
6	6	A	1
7	7	A	1
8	8	A	1
9	9	A	1
10	10	A	1
11	11	A	1
12	12	A	1
13	13	A	1
14	14	A	1
15	15	A	1
16	16	A	1
17	1	B	1
18	2	B	1
19	3	B	1
20	4	B	1
21	5	B	1
22	6	B	1
23	7	B	1
24	8	B	1
25	9	B	1
26	10	B	1
27	11	B	1
28	12	B	1
29	13	B	1
30	14	B	1
31	15	B	1
32	16	B	1
33	1	C	1
34	2	C	1
35	3	C	1
36	4	C	1
37	5	C	1
38	6	C	1
39	7	C	1
40	8	C	1
41	9	C	1
42	10	C	1
43	11	C	1
44	12	C	1
45	13	C	1
46	14	C	1
47	15	C	1
48	16	C	1
49	1	D	1
50	2	D	1
51	3	D	1
52	4	D	1
53	5	D	1
54	6	D	1
55	7	D	1
56	8	D	1
57	9	D	1
58	10	D	1
59	11	D	1
60	12	D	1
61	13	D	1
62	14	D	1
63	15	D	1
64	16	D	1
65	1	E	1
66	2	E	1
67	3	E	1
68	4	E	1
69	5	E	1
70	6	E	1
71	7	E	1
72	8	E	1
73	9	E	1
74	10	E	1
75	11	E	1
76	12	E	1
77	13	E	1
78	14	E	1
79	15	E	1
80	16	E	1
81	1	F	1
82	2	F	1
83	3	F	1
84	4	F	1
85	5	F	1
86	6	F	1
87	7	F	1
88	8	F	1
89	9	F	1
90	10	F	1
91	11	F	1
92	12	F	1
93	13	F	1
94	14	F	1
95	15	F	1
96	16	F	1
97	1	G	1
98	2	G	1
99	3	G	1
100	4	G	1
101	5	G	1
102	6	G	1
103	7	G	1
104	8	G	1
105	9	G	1
106	10	G	1
107	11	G	1
108	12	G	1
109	13	G	1
110	14	G	1
111	15	G	1
112	16	G	1
113	1	H	1
114	2	H	1
115	3	H	1
116	4	H	1
117	5	H	1
118	6	H	1
119	7	H	1
120	8	H	1
121	9	H	1
122	10	H	1
123	11	H	1
124	12	H	1
125	13	H	1
126	14	H	1
127	15	H	1
128	16	H	1
129	1	A	2
130	2	A	2
131	3	A	2
132	4	A	2
133	5	A	2
134	6	A	2
135	7	A	2
136	8	A	2
137	9	A	2
138	10	A	2
139	11	A	2
140	12	A	2
141	13	A	2
142	14	A	2
143	15	A	2
144	16	A	2
145	1	B	2
146	2	B	2
147	3	B	2
148	4	B	2
149	5	B	2
150	6	B	2
151	7	B	2
152	8	B	2
153	9	B	2
154	10	B	2
155	11	B	2
156	12	B	2
157	13	B	2
158	14	B	2
159	15	B	2
160	16	B	2
161	1	C	2
162	2	C	2
163	3	C	2
164	4	C	2
165	5	C	2
166	6	C	2
167	7	C	2
168	8	C	2
169	9	C	2
170	10	C	2
171	11	C	2
172	12	C	2
173	13	C	2
174	14	C	2
175	15	C	2
176	16	C	2
177	1	D	2
178	2	D	2
179	3	D	2
180	4	D	2
181	5	D	2
182	6	D	2
183	7	D	2
184	8	D	2
185	9	D	2
186	10	D	2
187	11	D	2
188	12	D	2
189	13	D	2
190	14	D	2
191	15	D	2
192	16	D	2
193	1	E	2
194	2	E	2
195	3	E	2
196	4	E	2
197	5	E	2
198	6	E	2
199	7	E	2
200	8	E	2
201	9	E	2
202	10	E	2
203	11	E	2
204	12	E	2
205	13	E	2
206	14	E	2
207	15	E	2
208	16	E	2
209	1	F	2
210	2	F	2
211	3	F	2
212	4	F	2
213	5	F	2
214	6	F	2
215	7	F	2
216	8	F	2
217	9	F	2
218	10	F	2
219	11	F	2
220	12	F	2
221	13	F	2
222	14	F	2
223	15	F	2
224	16	F	2
225	1	G	2
226	2	G	2
227	3	G	2
228	4	G	2
229	5	G	2
230	6	G	2
231	7	G	2
232	8	G	2
233	9	G	2
234	10	G	2
235	11	G	2
236	12	G	2
237	13	G	2
238	14	G	2
239	15	G	2
240	16	G	2
241	1	H	2
242	2	H	2
243	3	H	2
244	4	H	2
245	5	H	2
246	6	H	2
247	7	H	2
248	8	H	2
249	9	H	2
250	10	H	2
251	11	H	2
252	12	H	2
253	13	H	2
254	14	H	2
255	15	H	2
256	16	H	2
257	1	A	3
258	2	A	3
259	3	A	3
260	4	A	3
261	5	A	3
262	6	A	3
263	7	A	3
264	8	A	3
265	9	A	3
266	10	A	3
267	11	A	3
268	12	A	3
269	13	A	3
270	14	A	3
271	15	A	3
272	16	A	3
273	1	B	3
274	2	B	3
275	3	B	3
276	4	B	3
277	5	B	3
278	6	B	3
279	7	B	3
280	8	B	3
281	9	B	3
282	10	B	3
283	11	B	3
284	12	B	3
285	13	B	3
286	14	B	3
287	15	B	3
288	16	B	3
289	1	C	3
290	2	C	3
291	3	C	3
292	4	C	3
293	5	C	3
294	6	C	3
295	7	C	3
296	8	C	3
297	9	C	3
298	10	C	3
299	11	C	3
300	12	C	3
301	13	C	3
302	14	C	3
303	15	C	3
304	16	C	3
305	1	D	3
306	2	D	3
307	3	D	3
308	4	D	3
309	5	D	3
310	6	D	3
311	7	D	3
312	8	D	3
313	9	D	3
314	10	D	3
315	11	D	3
316	12	D	3
317	13	D	3
318	14	D	3
319	15	D	3
320	16	D	3
321	1	E	3
322	2	E	3
323	3	E	3
324	4	E	3
325	5	E	3
326	6	E	3
327	7	E	3
328	8	E	3
329	9	E	3
330	10	E	3
331	11	E	3
332	12	E	3
333	13	E	3
334	14	E	3
335	15	E	3
336	16	E	3
337	1	F	3
338	2	F	3
339	3	F	3
340	4	F	3
341	5	F	3
342	6	F	3
343	7	F	3
344	8	F	3
345	9	F	3
346	10	F	3
347	11	F	3
348	12	F	3
349	13	F	3
350	14	F	3
351	15	F	3
352	16	F	3
353	1	G	3
354	2	G	3
355	3	G	3
356	4	G	3
357	5	G	3
358	6	G	3
359	7	G	3
360	8	G	3
361	9	G	3
362	10	G	3
363	11	G	3
364	12	G	3
365	13	G	3
366	14	G	3
367	15	G	3
368	16	G	3
369	1	H	3
370	2	H	3
371	3	H	3
372	4	H	3
373	5	H	3
374	6	H	3
375	7	H	3
376	8	H	3
377	9	H	3
378	10	H	3
379	11	H	3
380	12	H	3
381	13	H	3
382	14	H	3
383	15	H	3
384	16	H	3
\.


--
-- TOC entry 3035 (class 0 OID 49241)
-- Dependencies: 206
-- Data for Name: Proiezioni; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

COPY multisala."Proiezioni" ("ID_Proiezione", "Ora_Inizio", "Cod_Film", "Cod_Sala", "Prezzo", "Proiezione", "Day") FROM stdin;
80	16:00:00	18	1	12.00	t	2022-02-24
35	09:30:00	18	1	12.00	t	2022-02-24
36	22:30:00	6	2	8.50	t	2022-02-24
37	08:30:00	8	3	5.00	t	2022-02-24
67	21:30:00	18	1	12.00	t	2022-02-24
81	09:30:00	7	2	12.00	t	2022-02-24
82	12:30:00	7	2	12.00	t	2022-02-24
85	08:30:00	8	3	12.00	t	2022-02-25
86	21:50:00	26	3	8.50	t	2022-03-03
88	09:30:00	6	1	9.00	t	2022-03-13
90	10:00:00	26	1	8.00	t	2022-03-13
91	11:00:00	26	2	10.00	t	2022-03-13
92	21:00:00	28	1	8.00	t	2022-03-21
\.


--
-- TOC entry 3037 (class 0 OID 49247)
-- Dependencies: 208
-- Data for Name: Sale; Type: TABLE DATA; Schema: multisala; Owner: postgres
--

COPY multisala."Sale" ("ID_Sala", "Nome", "Tecnologia") FROM stdin;
1	Alberto Sordi	IMAX Dolby
2	Vittorio Gassman	Dolby
3	Raffaele Viviani	IMAX
\.


--
-- TOC entry 3044 (class 0 OID 0)
-- Dependencies: 201
-- Name: Biglietti_ID_Biglietto_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Biglietti_ID_Biglietto_seq"', 612, true);


--
-- TOC entry 3045 (class 0 OID 0)
-- Dependencies: 203
-- Name: Film_ID_Film_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Film_ID_Film_seq"', 28, true);


--
-- TOC entry 3046 (class 0 OID 0)
-- Dependencies: 205
-- Name: Posti_ID_Posti_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Posti_ID_Posti_seq"', 384, true);


--
-- TOC entry 3047 (class 0 OID 0)
-- Dependencies: 207
-- Name: Proiezioni_ID_Proiezione_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Proiezioni_ID_Proiezione_seq"', 93, true);


--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 209
-- Name: Sale_ID_Sala_seq; Type: SEQUENCE SET; Schema: multisala; Owner: postgres
--

SELECT pg_catalog.setval('multisala."Sale_ID_Sala_seq"', 3, true);


--
-- TOC entry 2884 (class 2606 OID 49256)
-- Name: Biglietti Biglietti_pkey; Type: CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Biglietti"
    ADD CONSTRAINT "Biglietti_pkey" PRIMARY KEY ("ID_Biglietto");


--
-- TOC entry 2886 (class 2606 OID 49258)
-- Name: Film Film_pkey; Type: CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Film"
    ADD CONSTRAINT "Film_pkey" PRIMARY KEY ("ID_Film");


--
-- TOC entry 2888 (class 2606 OID 49260)
-- Name: Posti Posti_pkey; Type: CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Posti"
    ADD CONSTRAINT "Posti_pkey" PRIMARY KEY ("ID_Posti");


--
-- TOC entry 2890 (class 2606 OID 49262)
-- Name: Proiezioni Proiezioni_pkey; Type: CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Proiezioni"
    ADD CONSTRAINT "Proiezioni_pkey" PRIMARY KEY ("ID_Proiezione");


--
-- TOC entry 2892 (class 2606 OID 49264)
-- Name: Sale Sale_pkey; Type: CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Sale"
    ADD CONSTRAINT "Sale_pkey" PRIMARY KEY ("ID_Sala");


--
-- TOC entry 2898 (class 2620 OID 49265)
-- Name: Proiezioni trigger_check; Type: TRIGGER; Schema: multisala; Owner: postgres
--

CREATE TRIGGER trigger_check BEFORE INSERT ON multisala."Proiezioni" FOR EACH ROW EXECUTE FUNCTION multisala.check_film_proiezioni();


--
-- TOC entry 2896 (class 2606 OID 49266)
-- Name: Proiezioni fk_film; Type: FK CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Proiezioni"
    ADD CONSTRAINT fk_film FOREIGN KEY ("Cod_Film") REFERENCES multisala."Film"("ID_Film") ON DELETE CASCADE;


--
-- TOC entry 2893 (class 2606 OID 49271)
-- Name: Biglietti fk_posto; Type: FK CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Biglietti"
    ADD CONSTRAINT fk_posto FOREIGN KEY ("Cod_Posto") REFERENCES multisala."Posti"("ID_Posti");


--
-- TOC entry 2894 (class 2606 OID 49276)
-- Name: Biglietti fk_proiezione; Type: FK CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Biglietti"
    ADD CONSTRAINT fk_proiezione FOREIGN KEY ("Cod_Proiezione") REFERENCES multisala."Proiezioni"("ID_Proiezione") ON DELETE CASCADE;


--
-- TOC entry 2897 (class 2606 OID 49281)
-- Name: Proiezioni fk_sala; Type: FK CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Proiezioni"
    ADD CONSTRAINT fk_sala FOREIGN KEY ("Cod_Sala") REFERENCES multisala."Sale"("ID_Sala") ON DELETE CASCADE;


--
-- TOC entry 2895 (class 2606 OID 49286)
-- Name: Posti fk_sala; Type: FK CONSTRAINT; Schema: multisala; Owner: postgres
--

ALTER TABLE ONLY multisala."Posti"
    ADD CONSTRAINT fk_sala FOREIGN KEY ("Cod_Sala") REFERENCES multisala."Sale"("ID_Sala") ON DELETE CASCADE;

--
-- PostgreSQL database dump complete
--

