-- Example sections
-- (id, section)
INSERT INTO SECTIONS
VALUES (0, 'Section 1', '{"class":"org.amikhalev.sprinklers.service.impl.TestSection","data":{}}');
INSERT INTO SECTIONS
VALUES (1, 'Section 2', '{"class":"org.amikhalev.sprinklers.service.impl.TestSection","data":{}}');
INSERT INTO SECTIONS
VALUES (2, 'Section 3', '{"class":"org.amikhalev.sprinklers.service.impl.TestSection","data":{}}');
INSERT INTO SECTIONS
VALUES (3, 'Section 4', '{"class":"org.amikhalev.sprinklers.service.impl.TestSection","data":{}}');
INSERT INTO SECTIONS
VALUES (4, 'Section 5', '{"class":"org.amikhalev.sprinklers.service.impl.TestSection","data":{}}');
INSERT INTO SECTIONS
VALUES (5, 'Section 6', '{"class":"org.amikhalev.sprinklers.service.impl.GpioSection","data":{"port":4}}');

-- Example program
-- (id, name, schedule, enabled)
INSERT INTO PROGRAMS VALUES (0, 'main', 'at 6 am and 10 pm from 4/29 to 9/30', TRUE);

-- Example sections of program
-- (id, index, program_id, section_id, time)
INSERT INTO PROGRAMSECTIONS VALUES (0, 0, 0, 0, 2.0E0);
INSERT INTO PROGRAMSECTIONS VALUES (1, 1, 0, 1, 2.0E0);
INSERT INTO PROGRAMSECTIONS VALUES (2, 2, 0, 2, 2.0E0);
INSERT INTO PROGRAMSECTIONS VALUES (3, 3, 0, 3, 2.0E0);
INSERT INTO PROGRAMSECTIONS VALUES (4, 4, 0, 4, 2.0E0);
INSERT INTO PROGRAMSECTIONS VALUES (5, 5, 0, 5, 2.0E0);