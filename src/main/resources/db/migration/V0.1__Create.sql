CREATE SEQUENCE vp_sequence;

CREATE SEQUENCE employee_sequence;

CREATE TABLE employee
(
  id bigint NOT NULL,
  employee_id character varying(255),
  phone_number character varying(255),
  CONSTRAINT employee_pkey PRIMARY KEY (id),
  CONSTRAINT uk_mc5x07dj0uft9opsxchp0uwji UNIQUE (employee_id)

);

CREATE TABLE visitor_portal
(
  id bigint NOT NULL,
  badge_id character varying(255),
  checkin_time timestamp without time zone,
  checkout_time timestamp without time zone,
  employee_id bigint,
  CONSTRAINT visitor_portal_pkey PRIMARY KEY (id),
  CONSTRAINT fk8s0p7ivml6lg6oddold8xy375 FOREIGN KEY (employee_id)
    REFERENCES public.employee (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);