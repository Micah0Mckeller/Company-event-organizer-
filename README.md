# Company Event Organizer

A company event planner with a Java Spring Boot REST API and a Vite JavaScript
frontend. The public page lists events, and the separate admin page can create
and remove events through the shared API.

## Requirements

- JDK 25
- Node.js and npm

## Run locally

Start the backend in one terminal:

```powershell
cd backend/Java.backend
.\gradlew.bat bootRun
```

Start the frontend in a second terminal:

```powershell
cd "frontend/Event organizer"
npm install
npm run dev
```

Open the Vite URL shown in the terminal (usually `http://localhost:5173`).
The public events page is at `/`, and event management is at `/admin.html`.
The frontend calls `http://localhost:8080/api/events` by default. Set
`VITE_API_URL` in a frontend `.env` file to use a different API URL.

## Events API

- `GET /api/events` lists events.
- `POST /api/events` creates an event with a required `name` and an optional
  `description`. Dates and locations are not collected. The backend assigns
  an ID and queue timestamp.
- `DELETE /api/events/{id}` removes an event.

Events are held in an in-memory FIFO queue and returned in submission order.
The queue is cleared when the backend restarts. The legacy `Event.java` source
is excluded from compilation; event API objects use `EventRecord` instead.

## Tests

Run backend tests with:

```powershell
cd backend/Java.backend
.\gradlew.bat test
```

Build the frontend with:

```powershell
cd "frontend/Event organizer"
npm run build
```
