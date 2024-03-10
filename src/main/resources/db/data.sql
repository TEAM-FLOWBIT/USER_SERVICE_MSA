INSERT INTO member (created_at, deleted_at, updated_at, member_role, name, nickname, password, phone, profile, state, user_id)
SELECT CURRENT_TIMESTAMP, NULL, CURRENT_TIMESTAMP, 'ADMIN', '김민우', '김민우짱짱맨', '$2a$10$0OqDzJmWBlaLFKj1i1vtvOCP2UI.5UE3zJkfq2KlbDMIkIfUJh2m2', '010-1234-1234', 'flowbit-default-profile.png', true, 'kbsserver@naver.com'
    WHERE NOT EXISTS (SELECT 1 FROM member WHERE name = '김민우');
