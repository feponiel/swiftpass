## Entity-Relationship Model
![Database schema](.github/database-schema.svg)

## Application Endpoints

### Client-facing endpoints

#### Auth & Profile

<details>
  <summary><code>POST /login</code> Create a new session</summary>
  Future end-point diagram
</details>

<details>
  <summary><code>GET /me</code> Get your profile info</summary>
  Future end-point diagram
</details>

<details>
  <summary><code>PATCH /me</code> Edit your profile info</summary>
  Future end-point diagram
</details>

<details>
  <summary><code>DELETE /me</code> Delete your account</summary>
  Future end-point diagram
</details>

#### Events & Tickets
<details>
  <summary><code>GET /events/:eventId</code> Get a specific event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>PATCH /events/:eventId</code> Edit a specific event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>DELETE /events/:eventId</code> Delete a specific event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>POST /events/:eventId/tickets</code> Create a new ticket</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>GET /events/:eventId/tickets</code> List all tickets for an event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>GET /tickets/:ticketId</code> Get a specific ticket for an event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>PATCH /tickets/:ticketId</code> Edit a specific ticket for an event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>DELETE /tickets/:ticketId</code> Delete a specific ticket for an event</summary>
  Future end-point diagram
</details>

#### Subscriptions
<details>
  <summary><code>POST /events/:eventId/subscriptions</code> Register for an event</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>GET /events/:eventId/subscriptions?status={status}</code> List all event subscriptions, filtered by status or not</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>GET /subscriptions/me</code> Get your event subscriptions</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>PATCH /subscriptions/:subscriptionId</code> Edit a specific event subscription</summary>
  Future end-point diagram
</details>
<details>
  <summary><code>DELETE /subscriptions/:subscriptionId</code> Delete a specific event subscription</summary>
  Future end-point diagram
</details>

### System integration endpoints
<details>
  <summary><code>POST /webhooks/stripe</code> Send payment event notifications (Stripe via webhook)</summary>
  Future end-point diagram
</details>