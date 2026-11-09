/** Initializes a clickable counter and displays its current value. */
export function setupCounter(element) {
  let counter = 0
  // Updates both the stored count and the text shown in the element.
  const setCounter = (count) => {
    counter = count
    element.innerHTML = `Count is ${counter}`
  }
  // Advances the count whenever the element is clicked.
  element.addEventListener('click', () => setCounter(counter + 1))
  setCounter(0)
}
