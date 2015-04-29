DROP TABLE ProgramSections
IF EXISTS;
DROP TABLE Programs
IF EXISTS;
DROP TABLE Sections
IF EXISTS;

CREATE TABLE Programs
(
  id       INTEGER      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  name     VARCHAR(30)  NOT NULL,
  schedule VARCHAR(100) NOT NULL,
  enabled  BOOLEAN      NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE Sections
(
  id      INTEGER      NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  name    VARCHAR(30)  NOT NULL,
  section VARCHAR(200) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE ProgramSections
(
  id         INTEGER NOT NULL GENERATED BY DEFAULT AS IDENTITY,
  index      INTEGER,
  program_id INTEGER NOT NULL,
  section_id INTEGER NOT NULL,
  time       DOUBLE  NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (program_id) REFERENCES Programs (id),
  FOREIGN KEY (section_id) REFERENCES Sections (id)
);