export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/events'

/** Escapes HTML-sensitive characters before a value is inserted into page markup. */
export function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, (character) => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;',
  })[character])
}

/** Formats a date value for display, using a fallback for invalid dates. */
export function formatDate(date, options) {
  const parsedDate = date instanceof Date
    ? date
    : /^\d{4}-\d{2}-\d{2}$/.test(date)
      ? new Date(`${date}T00:00:00`)
      : new Date(date)
  if (Number.isNaN(parsedDate.getTime())) return 'Date to be confirmed'
  return options?.timeStyle ? parsedDate.toLocaleString(undefined, options) : parsedDate.toLocaleDateString(undefined, options)
}

/** Fetches the event list and validates that the API returned an array. */
export async function loadEvents() {
  const response = await fetch(API_URL)
  if (!response.ok) throw new Error(`Server returned ${response.status}`)
  const events = await response.json()
  if (!Array.isArray(events)) throw new Error('The events API returned an invalid response')
  return events
}
