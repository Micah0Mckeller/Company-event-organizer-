import './style.css'
import { API_URL, escapeHtml, formatDate, loadEvents } from './events.js'

const app = document.querySelector('#app')

app.innerHTML = `
  <header class="topbar admin-topbar">
    <a class="brand" href="/" aria-label="Company Event Organizer home">
      <span class="brand-mark" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="none"><path d="M12 19v-7m0 2c-4.5 0-7-2.5-7-7 4.5 0 7 2.5 7 7Zm0-3c0-4 2.4-6.5 7-7 0 4.5-2.4 7-7 7Z" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5"/></svg>
      </span>
      <span class="brand-name">Company Event Organizer</span>
      <span class="brand-divider">/</span>
      <span class="brand-section">Company events</span>
      <span class="admin-badge">ADMIN</span>
    </a>
    <a class="button button-light public-link" href="/"><span aria-hidden="true">↗</span> View public page</a>
  </header>
  <main class="admin-main">
    <section class="admin-heading">
      <div>
        <p class="eyebrow">YOUR WORKPLACE, TOGETHER</p>
        <h1>Manage events</h1>
        <p class="admin-subtitle">Create something to look forward to. Keep the team in the loop.</p>
      </div>
      <a class="button button-primary new-event-link" href="#event-form"><span aria-hidden="true">＋</span> New event</a>
    </section>
    <section class="admin-layout" aria-label="Manage company events">
      <form id="event-form" class="event-form">
        <div class="form-heading">
          <h2>New event</h2>
          <span class="status-badge">FIFO queue</span>
        </div>
        <p class="form-intro">Add an event to the queue. Events are listed in the order they are submitted.</p>
        <label>Event title *<input name="name" required maxlength="100" placeholder="e.g. Community garden morning"></label>
        <label>Description<textarea name="description" rows="4" maxlength="500" placeholder="What should everyone know?"></textarea></label>
        <p class="form-note"><span aria-hidden="true">ⓘ</span> New events are added to the end of the queue and shown on the public page.</p>
        <button class="submit-button" type="submit">Add to event queue <span aria-hidden="true">↗</span></button>
        <p id="form-message" class="message" role="status" aria-live="polite"></p>
      </form>
      <section class="events-panel" aria-labelledby="events-title">
        <div class="panel-heading">
          <div><h2 id="events-title">Your events</h2><p id="event-summary" class="panel-subtitle">Loading events</p></div>
          <label class="search-field admin-search">
            <span class="visually-hidden">Search your events</span>
            <span aria-hidden="true">⌕</span>
            <input id="admin-search" type="search" placeholder="Search events">
          </label>
        </div>
        <p class="admin-queue-note">First in, first shown</p>
        <div id="events-list" class="admin-event-list" aria-live="polite"></div>
        <div class="panel-footer"><span id="event-total">Showing 0 events</span><span>Events appear on the public page in queue order.</span></div>
      </section>
    </section>
    <p class="admin-footnote"><span aria-hidden="true">◎</span> The public page stays in sync with events you publish.</p>
  </main>
  <footer class="site-footer"><span>Company Event Organizer · Event administration</span><span>Events are shown in the order they were added.</span></footer>
`

const form = document.querySelector('#event-form')
const formMessage = document.querySelector('#form-message')
const eventsList = document.querySelector('#events-list')
const eventSummary = document.querySelector('#event-summary')
const eventTotal = document.querySelector('#event-total')
const searchInput = document.querySelector('#admin-search')
let events = []

/** Displays a form status message and applies error styling when needed. */
function setMessage(message, isError = false) {
  formMessage.textContent = message
  formMessage.classList.toggle('error', isError)
}

/** Filters and renders the admin event list and its queue counts. */
function renderAdminEvents() {
  const query = searchInput.value.trim().toLocaleLowerCase()
  const filteredEvents = events.filter((event) =>
      [event.name, event.description].some((value) =>
        String(value || '').toLocaleLowerCase().includes(query)))

  eventSummary.textContent = `${events.length} ${events.length === 1 ? 'event' : 'events'} in queue`
  eventTotal.textContent = `Showing ${filteredEvents.length} ${filteredEvents.length === 1 ? 'event' : 'events'}`

  if (!filteredEvents.length) {
    eventsList.innerHTML = `<div class="admin-empty"><span aria-hidden="true">✳</span><p>${events.length ? 'No events match your search.' : 'No events have been added yet.'}</p><p>${events.length ? 'Try another search.' : 'Use the form to plan the first one.'}</p></div>`
    return
  }

  eventsList.innerHTML = filteredEvents.map((event) => {
    return `
      <article class="admin-event-row">
        <div class="admin-event-main">
          <div class="admin-event-details">
            <h3>${escapeHtml(event.name)}</h3>
            <p>Added ${escapeHtml(formatDate(event.createdAt, { dateStyle: 'medium', timeStyle: 'short' }))}</p>
          </div>
          <span class="queue-position">#${events.indexOf(event) + 1}</span>
        </div>
        ${event.description ? `<p class="admin-event-description">${escapeHtml(event.description)}</p>` : ''}
        <div class="admin-event-actions">
          <span class="published-note"><span aria-hidden="true">◎</span> Visible on the public page</span>
          ${event.id ? `<button class="button button-light remove-button" type="button" data-event-id="${escapeHtml(event.id)}">Remove</button>` : ''}
        </div>
      </article>
    `
  }).join('')
}

// Keep the event list filtered as the administrator types.
searchInput.addEventListener('input', renderAdminEvents)

// Submits a new event to the backend and refreshes the list on success.
form.addEventListener('submit', async (event) => {
  event.preventDefault()
  const submitButton = form.querySelector('button[type="submit"]')
  submitButton.disabled = true
  setMessage('')

  const formData = new FormData(form)
  const newEvent = {
    name: String(formData.get('name')).trim(),
    description: String(formData.get('description')).trim(),
  }

  try {
    const response = await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newEvent),
    })
    if (!response.ok) {
      throw new Error(response.status === 400
        ? 'An event title is required.'
        : `Server returned ${response.status}`)
    }
    form.reset()
    setMessage('Event added to the queue and shown on the public page.')
    await refreshEvents()
  } catch (error) {
    setMessage(`Could not publish the event. ${error.message}`, true)
    console.error('Could not create event:', error)
  } finally {
    submitButton.disabled = false
  }
})

// Removes the selected event and refreshes the list after deletion.
eventsList.addEventListener('click', async (event) => {
  const button = event.target.closest('[data-event-id]')
  if (!button) return

  button.disabled = true
  try {
    const response = await fetch(`${API_URL}/${encodeURIComponent(button.dataset.eventId)}`, { method: 'DELETE' })
    if (!response.ok) throw new Error(`Server returned ${response.status}`)
    await refreshEvents()
  } catch (error) {
    button.disabled = false
    setMessage(`Could not remove the event. ${error.message}`, true)
    console.error('Could not delete event:', error)
  }
})

/** Reloads events from the backend and updates the admin list. */
async function refreshEvents() {
  events = await loadEvents()
  renderAdminEvents()
}

eventsList.innerHTML = '<p class="loading">Loading events…</p>'
// Show a connection error when the initial event-list request fails.
refreshEvents().catch((error) => {
  eventsList.innerHTML = '<p class="connection-error">Could not load events. Make sure the Java backend is running, then refresh.</p>'
  eventSummary.textContent = 'Events unavailable'
  eventTotal.textContent = 'Could not load events'
  console.error('Could not load events:', error)
})
