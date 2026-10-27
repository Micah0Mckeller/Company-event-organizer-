export const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/events'

export function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, (character) => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;',
  })[character])
}

export function formatDate(date, options) {
  const parsedDate = date instanceof Date
    ? date
    : /^\d{4}-\d{2}-\d{2}$/.test(date)
      ? new Date(`${date}T00:00:00`)
      : new Date(date)
  if (Number.isNaN(parsedDate.getTime())) return 'Date to be confirmed'
  return options?.timeStyle ? parsedDate.toLocaleString(undefined, options) : parsedDate.toLocaleDateString(undefined, options)
}

export async function loadEvents() {
  const response = await fetch(API_URL)
  if (!response.ok) throw new Error(`Server returned ${response.status}`)
  const events = await response.json()
  if (!Array.isArray(events)) throw new Error('The events API returned an invalid response')
  return events
}
