-- 1. Insert Users (Admin, PO, SM, Devs)
INSERT INTO public.users (id, created_date, email, is_email_verified, is_enabled, first_name, last_name, is_account_locked, is_crendetial_expired, password) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'admin@techcorp.com', true, true, 'System', 'Admin', false, false, 'password'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'sarah.po@techcorp.com', true, true, 'Sarah', 'Jenkins', false, false, 'password'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'mike.sm@techcorp.com', true, true, 'Mike', 'Ross', false, false, 'password'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', NOW(), 'david.dev@techcorp.com', true, true, 'David', 'Chen', false, false, 'password'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', NOW(), 'emily.dev@techcorp.com', true, true, 'Emily', 'Watson', false, false, 'password');

-- 2. Insert Product Backlog
INSERT INTO public.product_backlogs (id, created_by, created_date, description, name) VALUES
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'Migration of legacy monolithic store to microservices architecture.', 'E-Commerce 2.0 Refactor');

-- 3. Insert Project Members
-- Product Owner, Scrum Master, and Developers must all be project members
INSERT INTO public.project_members (id, created_by, created_date, role, status, product_backlog_id, user_id) VALUES
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'PRODUCT_OWNER', 'ACTIVE', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'SCRUM_MASTER', 'ACTIVE', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'DEVELOPER', 'ACTIVE', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a01', NOW(), 'DEVELOPER', 'ACTIVE', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05');

-- 4. Insert Epics
-- Created by Product Owner (a02) - Rule: CREATE: Project Owner
INSERT INTO public.epics (id, created_by, created_date, description, title, product_backlog_id) VALUES
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'Handle user registration, login, and JWT management.', 'Authentication Module', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01'),
('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c02', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'Shopping cart state management and persistence.', 'Cart Management', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01');

-- 5. Insert Sprint Backlogs
-- Created by Scrum Master (a03) - Rule: CREATE: Project Owner, Scrum Master
INSERT INTO public.sprint_backlogs (id, created_by, created_date, end_date, goal, name, start_date, product_backlog_id, scrum_master_id) VALUES
('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), CURRENT_DATE + INTERVAL '14 days', 'Establish auth foundation and basic cart structure.', 'Sprint 1 - Foundation', CURRENT_DATE, 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03');

-- 6. Insert Sprint Members
-- Developers (a04, a05) are sprint members
-- Note: Scrum Master (a03) is automatically a sprint member via scrum_master_id
INSERT INTO public.sprint_members (id, created_by, created_date, sprint_backlog_id, user_id) VALUES
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03');

-- 7. Insert Sprint History
-- Created by Scrum Master who is sprint member
INSERT INTO public.sprint_histories (id, created_by, created_date, note, status, sprint_backlog_id) VALUES
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'Sprint started on time.', 'ACTIVE', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01');

-- 8. Insert User Stories
-- Created by Product Owner (a02) - Rule: CREATE: Project Owner
INSERT INTO public.user_stories (id, created_by, created_date, acceptance_criteria, description, priority, story_points, title, epic_id, product_backlog_id, sprint_backlog_id) VALUES
-- Story 1: Login (Assigned to Sprint 1)
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'User can login with email/pass; receives JWT; errors handled.', 'As a user, I want to login so I can access my order history.', 'MUST_HAVE', 5, 'User Login API', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01'),
-- Story 2: Add to Cart (Assigned to Sprint 1)
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e02', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'Item appears in cart list; Total price updates.', 'As a customer, I want to add items to my cart.', 'SHOULD_HAVE', 8, 'Add Item to Cart', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c02', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01'),
-- Story 3: Password Reset (Backlog only)
('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e03', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a02', NOW(), 'Email link sent; Token validation.', 'As a user, I want to reset my password.', 'COULD_HAVE', 3, 'Password Reset Flow', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380c01', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b01', NULL);

-- 9. Insert User Story Histories
-- Status updates by Scrum Master (a03) who is sprint member
-- Rule: UPDATE_STATUS: Scrum Master, Developer (must be sprint member)
INSERT INTO public.user_story_histories (id, created_by, created_date, note, status, user_story_id) VALUES
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'Moved to development.', 'IN_PROGRESS', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a03', NOW(), 'Ready for development.', 'TODO', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e02');

-- 10. Insert Tasks
-- Created by Developers who are sprint members
-- Rule: CREATE: Project Owner, Scrum Master, Developer (must be sprint member)
INSERT INTO public.tasks (id, created_by, created_date, actual_hours, description, estimated_hours, title, assignee_id, sprint_backlog_id, user_story_id) VALUES
-- Task for Story 1 (Login) - Created by Developer (a04) who is sprint member
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f01', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', NOW(), 2.5, 'Implement /api/auth/login endpoint with bcrypt validation.', 4.0, 'Backend Login Logic', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01'),
-- Task for Story 1 (Login) - Created by Developer (a05) who is sprint member
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f02', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', NOW(), 0.0, 'Create React form for Login with error states.', 6.0, 'Frontend Login Form', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01'),
-- Task for Story 1 (Login) - Created by Developer (a05) who is sprint member
('f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f03', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', NOW(), 2.5, 'Implement /api/auth/logout endpoint.', 2.0, 'Backend Logout Logic', NULL, 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380d01', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380e01');

-- 11. Insert Task Histories
-- Status updates by assignees who are sprint members
-- Rule: UPDATE_STATUS: Scrum Master, Developer (must be sprint member)
INSERT INTO public.task_histories (id, created_by, created_date, note, status, task_id) VALUES
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a04', NOW(), 'Started working on API.', 'IN_PROGRESS', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f01'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', NOW(), 'Assigned but not started.', 'ASSIGNED', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f02'),
(gen_random_uuid(), 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a05', NOW(), 'just created and not Assigned.', 'NEW', 'f0eebc99-9c0b-4ef8-bb6d-6bb9bd380f03');
