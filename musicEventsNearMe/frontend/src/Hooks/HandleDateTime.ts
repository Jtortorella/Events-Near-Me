export function convertLocalDateTimeToDateTime(dateTime: string) {
  const dateConversion = new Date(dateTime);
  return dateConversion.toLocaleString("en-US", {
    year: "numeric",
    month: "long",
    day: "numeric",
    hour: "numeric",
    minute: "numeric",
    hour12: true,
  });
}

