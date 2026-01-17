--
-- PostgreSQL database dump
--

\restrict pFJiqvz0QL9CXAfNLJ7pwptDKbwcukkNCG5yxl2dM6YAxSnQ6jM7QiRxFnSbtmi

-- Dumped from database version 18.0 (Debian 18.0-1.pgdg13+3)
-- Dumped by pg_dump version 18.1

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: epics; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.epics (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    description character varying(255),
    title character varying(255),
    product_backlog_id uuid NOT NULL
);


ALTER TABLE public.epics OWNER TO admin;

--
-- Name: product_backlogs; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.product_backlogs (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    description character varying(255) NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.product_backlogs OWNER TO admin;

--
-- Name: project_members; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.project_members (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    role character varying(255),
    status character varying(255),
    product_backlog_id uuid NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT project_members_role_check CHECK (((role)::text = ANY ((ARRAY['PRODUCT_OWNER'::character varying, 'SCRUM_MASTER'::character varying, 'DEVELOPER'::character varying, 'MEMBER'::character varying])::text[]))),
    CONSTRAINT project_members_status_check CHECK (((status)::text = ANY ((ARRAY['INVITED'::character varying, 'ACTIVE'::character varying, 'INACTIVE'::character varying])::text[])))
);


ALTER TABLE public.project_members OWNER TO admin;

--
-- Name: sprint_backlogs; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.sprint_backlogs (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    end_date date NOT NULL,
    goal character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    start_date date NOT NULL,
    product_backlog_id uuid NOT NULL,
    scrum_master_id uuid NOT NULL
);


ALTER TABLE public.sprint_backlogs OWNER TO admin;

--
-- Name: sprint_histories; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.sprint_histories (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    note character varying(255),
    status character varying(255),
    sprint_backlog_id uuid NOT NULL,
    CONSTRAINT sprint_histories_status_check CHECK (((status)::text = ANY ((ARRAY['PLANNED'::character varying, 'ACTIVE'::character varying, 'COMPLETED'::character varying, 'CANCELLED'::character varying])::text[])))
);


ALTER TABLE public.sprint_histories OWNER TO admin;

--
-- Name: sprint_members; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.sprint_members (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    sprint_backlog_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.sprint_members OWNER TO admin;

--
-- Name: task_histories; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.task_histories (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    note character varying(255),
    status character varying(255),
    task_id uuid NOT NULL,
    CONSTRAINT task_histories_status_check CHECK (((status)::text = ANY ((ARRAY['NEW'::character varying, 'ASSIGNED'::character varying, 'TODO'::character varying, 'IN_PROGRESS'::character varying, 'IN_TEST'::character varying, 'BLOCKED'::character varying, 'DONE'::character varying])::text[])))
);


ALTER TABLE public.task_histories OWNER TO admin;

--
-- Name: tasks; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.tasks (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    actual_hours double precision,
    description character varying(255) NOT NULL,
    estimated_hours double precision NOT NULL,
    title character varying(255) NOT NULL,
    assignee_id uuid,
    sprint_backlog_id uuid NOT NULL,
    user_story_id uuid NOT NULL
);


ALTER TABLE public.tasks OWNER TO admin;

--
-- Name: user_stories; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.user_stories (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    acceptance_criteria character varying(255),
    description character varying(255) NOT NULL,
    priority character varying(255) NOT NULL,
    story_points integer,
    title character varying(255) NOT NULL,
    epic_id uuid,
    product_backlog_id uuid NOT NULL,
    sprint_backlog_id uuid,
    CONSTRAINT user_stories_priority_check CHECK (((priority)::text = ANY ((ARRAY['MUST_HAVE'::character varying, 'SHOULD_HAVE'::character varying, 'COULD_HAVE'::character varying, 'WONT_HAVE'::character varying])::text[])))
);


ALTER TABLE public.user_stories OWNER TO admin;

--
-- Name: user_story_histories; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.user_story_histories (
    id uuid NOT NULL,
    created_by uuid NOT NULL,
    created_date timestamp(6) without time zone NOT NULL,
    last_modified_by uuid,
    last_modified_date timestamp(6) without time zone,
    note character varying(255),
    status character varying(255),
    user_story_id uuid NOT NULL,
    CONSTRAINT user_story_histories_status_check CHECK (((status)::text = ANY ((ARRAY['TODO'::character varying, 'IN_PROGRESS'::character varying, 'DONE'::character varying])::text[])))
);


ALTER TABLE public.user_story_histories OWNER TO admin;

--
-- Name: users; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_date date NOT NULL,
    is_crendetial_expired boolean,
    email character varying(255) NOT NULL,
    is_email_verified boolean,
    is_enabled boolean,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    is_account_locked boolean,
    password character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO admin;

--
-- Name: epics epics_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.epics
    ADD CONSTRAINT epics_pkey PRIMARY KEY (id);


--
-- Name: product_backlogs product_backlogs_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.product_backlogs
    ADD CONSTRAINT product_backlogs_pkey PRIMARY KEY (id);


--
-- Name: project_members project_members_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT project_members_pkey PRIMARY KEY (id);


--
-- Name: sprint_backlogs sprint_backlogs_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_backlogs
    ADD CONSTRAINT sprint_backlogs_pkey PRIMARY KEY (id);


--
-- Name: sprint_histories sprint_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_histories
    ADD CONSTRAINT sprint_histories_pkey PRIMARY KEY (id);


--
-- Name: sprint_members sprint_members_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_members
    ADD CONSTRAINT sprint_members_pkey PRIMARY KEY (id);


--
-- Name: task_histories task_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.task_histories
    ADD CONSTRAINT task_histories_pkey PRIMARY KEY (id);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: epics uk_epic_title_product_backlog; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.epics
    ADD CONSTRAINT uk_epic_title_product_backlog UNIQUE (title, product_backlog_id);


--
-- Name: sprint_backlogs uk_sprint_name_product_backlog; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_backlogs
    ADD CONSTRAINT uk_sprint_name_product_backlog UNIQUE (name, product_backlog_id);


--
-- Name: tasks uk_tiltle_user_story; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT uk_tiltle_user_story UNIQUE (title, user_story_id);


--
-- Name: project_members uk_user_project; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT uk_user_project UNIQUE (user_id, product_backlog_id);


--
-- Name: sprint_members uk_user_sprint; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_members
    ADD CONSTRAINT uk_user_sprint UNIQUE (user_id, sprint_backlog_id);


--
-- Name: user_stories uk_user_story_title_product_backlog; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_stories
    ADD CONSTRAINT uk_user_story_title_product_backlog UNIQUE (title, product_backlog_id);


--
-- Name: user_stories user_stories_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_stories
    ADD CONSTRAINT user_stories_pkey PRIMARY KEY (id);


--
-- Name: user_story_histories user_story_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_story_histories
    ADD CONSTRAINT user_story_histories_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx_epic_product_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_epic_product_backlog ON public.epics USING btree (product_backlog_id);


--
-- Name: idx_project_member_product_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_project_member_product_backlog ON public.project_members USING btree (product_backlog_id);


--
-- Name: idx_project_member_user; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_project_member_user ON public.project_members USING btree (user_id);


--
-- Name: idx_sprint_backlog_product_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_sprint_backlog_product_backlog ON public.sprint_backlogs USING btree (product_backlog_id);


--
-- Name: idx_sprint_backlog_scrum_master; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_sprint_backlog_scrum_master ON public.sprint_backlogs USING btree (scrum_master_id);


--
-- Name: idx_sprint_history_sprint_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_sprint_history_sprint_backlog ON public.sprint_histories USING btree (sprint_backlog_id);


--
-- Name: idx_sprint_member_sprint_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_sprint_member_sprint_backlog ON public.sprint_members USING btree (sprint_backlog_id);


--
-- Name: idx_sprint_member_user; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_sprint_member_user ON public.sprint_members USING btree (user_id);


--
-- Name: idx_task_assignee; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_task_assignee ON public.tasks USING btree (assignee_id);


--
-- Name: idx_task_history_task; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_task_history_task ON public.task_histories USING btree (task_id);


--
-- Name: idx_task_sprint_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_task_sprint_backlog ON public.tasks USING btree (sprint_backlog_id);


--
-- Name: idx_task_user_story; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_task_user_story ON public.tasks USING btree (user_story_id);


--
-- Name: idx_user_story_epic; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_user_story_epic ON public.user_stories USING btree (epic_id);


--
-- Name: idx_user_story_history_user_story; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_user_story_history_user_story ON public.user_story_histories USING btree (user_story_id);


--
-- Name: idx_user_story_product_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_user_story_product_backlog ON public.user_stories USING btree (product_backlog_id);


--
-- Name: idx_user_story_sprint_backlog; Type: INDEX; Schema: public; Owner: admin
--

CREATE INDEX idx_user_story_sprint_backlog ON public.user_stories USING btree (sprint_backlog_id);


--
-- Name: sprint_backlogs fk1rf11xmsv7joe6bcci0yg2nhg; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_backlogs
    ADD CONSTRAINT fk1rf11xmsv7joe6bcci0yg2nhg FOREIGN KEY (product_backlog_id) REFERENCES public.product_backlogs(id);


--
-- Name: tasks fk298w1wm4fuqrrxf0ncea3j02g; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk298w1wm4fuqrrxf0ncea3j02g FOREIGN KEY (sprint_backlog_id) REFERENCES public.sprint_backlogs(id);


--
-- Name: sprint_histories fk4lexhm37q7lv2odaen87iom4e; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_histories
    ADD CONSTRAINT fk4lexhm37q7lv2odaen87iom4e FOREIGN KEY (sprint_backlog_id) REFERENCES public.sprint_backlogs(id);


--
-- Name: sprint_members fk87w93ihhh7cnq7lbxm86dwwlt; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_members
    ADD CONSTRAINT fk87w93ihhh7cnq7lbxm86dwwlt FOREIGN KEY (sprint_backlog_id) REFERENCES public.sprint_backlogs(id);


--
-- Name: project_members fk8ifjg82c87e6cwo470gt4sanr; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT fk8ifjg82c87e6cwo470gt4sanr FOREIGN KEY (product_backlog_id) REFERENCES public.product_backlogs(id);


--
-- Name: sprint_members fkajv008vu6ie98wblsnr4fv22m; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_members
    ADD CONSTRAINT fkajv008vu6ie98wblsnr4fv22m FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: tasks fkekr1dgiqktpyoip3qmp6lxsit; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fkekr1dgiqktpyoip3qmp6lxsit FOREIGN KEY (assignee_id) REFERENCES public.users(id);


--
-- Name: user_story_histories fkgtjyeojmnwr6lbxjjh0j9w51o; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_story_histories
    ADD CONSTRAINT fkgtjyeojmnwr6lbxjjh0j9w51o FOREIGN KEY (user_story_id) REFERENCES public.user_stories(id);


--
-- Name: project_members fkgul2el0qjk5lsvig3wgajwm77; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.project_members
    ADD CONSTRAINT fkgul2el0qjk5lsvig3wgajwm77 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: epics fkiah1nnoo8imq7koi5j02guf3y; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.epics
    ADD CONSTRAINT fkiah1nnoo8imq7koi5j02guf3y FOREIGN KEY (product_backlog_id) REFERENCES public.product_backlogs(id);


--
-- Name: tasks fkkhukd6i4si1sthgxf7k5g0383; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fkkhukd6i4si1sthgxf7k5g0383 FOREIGN KEY (user_story_id) REFERENCES public.user_stories(id);


--
-- Name: user_stories fklg3eomprb6x89ybt3gv2mja73; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_stories
    ADD CONSTRAINT fklg3eomprb6x89ybt3gv2mja73 FOREIGN KEY (epic_id) REFERENCES public.epics(id);


--
-- Name: task_histories fkmacu1ui1wsdfvrow9y358v5u5; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.task_histories
    ADD CONSTRAINT fkmacu1ui1wsdfvrow9y358v5u5 FOREIGN KEY (task_id) REFERENCES public.tasks(id);


--
-- Name: sprint_backlogs fkmjfg5bcbu343tald8hc462kh6; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.sprint_backlogs
    ADD CONSTRAINT fkmjfg5bcbu343tald8hc462kh6 FOREIGN KEY (scrum_master_id) REFERENCES public.users(id);


--
-- Name: user_stories fkne41q78cm0s01q2nxn43kof7v; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_stories
    ADD CONSTRAINT fkne41q78cm0s01q2nxn43kof7v FOREIGN KEY (sprint_backlog_id) REFERENCES public.sprint_backlogs(id);


--
-- Name: user_stories fkpqacw8fyss9127mv7jxajdpxm; Type: FK CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.user_stories
    ADD CONSTRAINT fkpqacw8fyss9127mv7jxajdpxm FOREIGN KEY (product_backlog_id) REFERENCES public.product_backlogs(id);


--
-- PostgreSQL database dump complete
--

\unrestrict pFJiqvz0QL9CXAfNLJ7pwptDKbwcukkNCG5yxl2dM6YAxSnQ6jM7QiRxFnSbtmi
