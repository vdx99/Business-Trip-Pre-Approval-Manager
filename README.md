# Business Trip Request (BTR)
A Spring Boot web application for managing business trip requests with role-based approval workflow. The system supports three main roles: standard User, Approver, and Admin, each with their own dashboard and permissions.

## Features
Business Trip Request (BTR) creation with form validation (dates, amounts, required fields).
​

Automatic status assignment based on requester role (C-Level vs regular user) and configurable amount threshold.

Role-based access control with Spring Security (USER, APPROVER, ADMIN) and login redirects to dedicated dashboards.

Admin panel for managing user roles and application settings (approval threshold stored in the database).

Approver dashboard for reviewing and changing status of pending requests.

Thymeleaf-based UI with server-side rendering and integration with Spring Security tags.
​
​

## Tech Stack
Backend: Spring Boot, Spring MVC, Spring Security

View layer: Thymeleaf + Spring Security Thymeleaf extras

Persistence: Spring Data JPA, Hibernate, relational database (e.g. H2/PostgreSQL/MySQL)

Validation: Jakarta Bean Validation (JSR-380) annotations for form inputs.
​

Build tool: Maven

## Business Logic Overview
Users can create a Business Trip Request specifying destination, dates, purpose and anticipated expense amount.

Basic validation ensures that the end date is not before the start date and that numeric fields respect defined constraints.
​

If the requester is in the C-Level Management role, the request is automatically approved.

### For other users:

If amount ≤ threshold → status APPROVED.

If amount ≥ 2 × threshold → status REJECTED.

Otherwise → status PENDING and routed to an Approver.

The approval threshold is not hardcoded; it is stored in a dedicated settings entity and can be changed at runtime by an Admin from the UI.

## Roles and Permissions
Role	        Capabilities
USER	        Create and view own BTRs on /btr/dashboard.
APPROVER	    Access approver dashboard, view pending requests, change status to approved/rejected.
ADMIN	        Manage users and roles on /admin/users, edit application settings (approval threshold) on /admin/settings.
Access to views and actions is protected using Spring Security configuration and Thymeleaf sec:authorize expressions.
​
​

## Application Structure
controller – MVC controllers for BTR operations, admin panel and dashboards.

model – JPA entities such as User, Role, BusinessTripRequest, and AppSettings.

repository – Spring Data JPA repositories for database access.

service – Business services for current user handling, settings management and domain logic.

This structure follows common Spring Boot best practices and keeps controllers thin by delegating logic to services.

## Getting Started
Clone the repository.

Configure database connection in application.properties (or use in-memory H2 for development).

Run the Spring Boot application (e.g. via your IDE or mvn spring-boot:run).

Open the app in a browser (e.g. http://localhost:8080) and log in with one of the predefined users.

You can then:

As a User: create new BTRs and see their status on the dashboard.

As an Approver: work with pending requests in the approver dashboard.

As an Admin: adjust roles for existing users and change the approval threshold from the Admin Settings page.

## Possible Improvements
Add REST API endpoints for integration with other systems.
Introduce pagination and filtering for large BTR lists.
Add email notifications when a request changes status.
Add tests (unit and integration) for controllers, services and security configuration.

