export const filterEntries = (
  entries,
  setFilteredEntries,
  dateRange,
  authorsSelected
) => {
  if (
    (dateRange[0] !== null && dateRange[1] !== null) ||
    authorsSelected.length > 0
  ) {
    let filteredEntries;
    // only authors selected
    if (
      authorsSelected.length > 0 &&
      !(dateRange[0] !== null && dateRange[1] !== null)
    ) {
      filteredEntries = entries.filter((entry) =>
        authorsSelected.some((author) => author.label === entry.name)
      );
    }
    // only dates selected
    else if (
      !authorsSelected.length > 0 &&
      dateRange[0] !== null &&
      dateRange[1] !== null
    ) {
      filteredEntries = entries.filter(
        (e) =>
          new Date(e.date) >= new Date(dateRange[0]) &&
          new Date(e.date) <= new Date(dateRange[1])
      );
    }
    // both dates and authors selected
    else {
      filteredEntries = entries.filter(
        (entry) =>
          new Date(entry.date) >= new Date(dateRange[0]) &&
          new Date(entry.date) <= new Date(dateRange[1]) &&
          authorsSelected.some((author) => author.label === entry.name)
      );
    }
    setFilteredEntries(filteredEntries);
  } else {
    setFilteredEntries(entries);
  }
};
