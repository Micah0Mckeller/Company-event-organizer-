import './style.css'
import { escapeHtml, formatDate, loadEvents } from './events.js'

const app = document.querySelector('#app')

app.innerHTML = `
  <header class="topbar">
    <a class="brand" href="/" aria-label="Company Event Organizer home">
      <span class="brand-mark" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="none"><path d="M12 19v-7m0 2c-4.5 0-7-2.5-7-7 4.5 0 7 2.5 7 7Zm0-3c0-4 2.4-6.5 7-7 0 4.5-2.4 7-7 7Z" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/></svg>
      </span>
      <span class="brand-name">Company Event Organizer</span>
      <span class="brand-divider">/</span>
      <span class="brand-section">Company events</span>
    </a>
    <nav class="site-nav" aria-label="Main navigation">
      <a class="nav-link active" href="/">Events</a>
      <a class="nav-link" href="/admin.html">Manage events</a>
      <a class="nav-contact" href="mailto:events@company.example"><span aria-hidden="true">✉</span> Get in touch</a>
    </nav>
  </header>
  <main>
    <section class="public-intro">
      <div>
        <p class="eyebrow">LIFE AT COMPANY</p>
        <h1>Good things happen<br>when we get together.</h1>
      </div>
      <div class="intro-aside">
        <p>A place to connect, share ideas, and make a little room for fun. See what’s coming up across our company.</p>
        <p class="aside-note"><span aria-hidden="true">↗</span> Events, in the order they were added</p>
      </div>
    </section>
    <section class="event-controls" aria-label="Find an event">
      <p class="queue-order-label"><span aria-hidden="true">↳</span> First in, first shown</p>
      <label class="search-field">
        <span class="visually-hidden">Search events</span>
        <span aria-hidden="true">⌕</span>
        <input id="event-search" type="search" placeholder="Search events">
      </label>
    </section>
    <section class="public-events" aria-labelledby="events-title">
      <div class="section-heading">
        <h2 id="events-title">The event queue</h2>
        <span id="event-count" class="event-count">Loading events</span>
      </div>
      <div id="events-list" class="public-event-list" aria-live="polite"></div>
    </section>
    <p class="welcome-note">Events are shown in the order they were added. Everyone is welcome.</p>
  </main>
  <footer class="site-footer"><span>Company Event Organizer · Better together, every day.</span><span>Questions? <a href="mailto:events@company.example">events@company.example</a></span></footer>
`

const eventsList = document.querySelector('#events-list')
const eventCount = document.querySelector('#event-count')
const searchInput = document.querySelector('#event-search')
let events = []

/** Filters events by the search field and renders the public event queue. */
function renderEvents() {
  const query = searchInput.value.trim().toLocaleLowerCase()
  const filteredEvents = events.filter((event) =>
    [event.name, event.description].some((value) =>
      String(value || '').toLocaleLowerCase().includes(query)))

  eventCount.textContent = `${filteredEvents.length} ${filteredEvents.length === 1 ? 'event' : 'events'}`
  if (!filteredEvents.length) {
    eventsList.innerHTML = `<div class="empty-state"><span aria-hidden="true">✳</span><p>${events.length ? 'No events match your search.' : 'Your event queue is empty.'}</p><p>${events.length ? 'Try another search.' : 'Check back soon for the next event.'}</p></div>`
    return
  }

  const [firstEvent, ...remainingEvents] = filteredEvents
  eventsList.innerHTML = `
    <article class="featured-event">
      <div class="event-date-block"><span>QUEUE</span><strong>#${events.indexOf(firstEvent) + 1}</strong><span>FIRST</span></div>
      <div class="featured-details">
        <p class="event-kicker"><span aria-hidden="true">✳</span> FIRST IN QUEUE</p>
        <h3>${escapeHtml(firstEvent.name)}</h3>
        ${firstEvent.description ? `<p class="featured-description">${escapeHtml(firstEvent.description)}</p>` : ''}
        <p class="featured-meta"><span aria-hidden="true">↳</span> Added ${escapeHtml(formatDate(firstEvent.createdAt, { dateStyle: 'medium', timeStyle: 'short' }))}</p>
      </div>
    </article>
    ${remainingEvents.length ? `<div class="event-grid">${remainingEvents.map((event) => `
      <article class="event-card">
        <div class="event-card-content">
          <div class="event-card-top"><span class="event-tag">Queue #${events.indexOf(event) + 1}</span></div>
          <h3>${escapeHtml(event.name)}</h3>
          ${event.description ? `<p class="event-card-description">${escapeHtml(event.description)}</p>` : ''}
          <p class="event-meta">Added ${escapeHtml(formatDate(event.createdAt, { dateStyle: 'medium', timeStyle: 'short' }))}</p>
        </div>
      </article>
    `).join('')}</div>` : ''}
  `
}

// Re-render the queue whenever the search query changes.
searchInput.addEventListener('input', renderEvents)
eventsList.innerHTML = '<p class="loading">Loading events…</p>'

// Display the event queue after loading it from the backend.
loadEvents()
  .then((loadedEvents) => {
    events = loadedEvents
    renderEvents()
  })
  // Show a user-facing error and log details if the backend request fails.
  .catch((error) => {
    eventsList.innerHTML = '<p class="connection-error">Could not load events. Make sure the Java backend is running, then refresh.</p>'
    eventCount.textContent = 'Events unavailable'
    console.error('Could not load events:', error)
  })
