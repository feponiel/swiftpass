ALTER TABLE tickets
  ADD CONSTRAINT fk_tickets_event_id
  FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE;

ALTER TABLE registrations
  ADD CONSTRAINT fk_registrations_event_id
  FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE;
