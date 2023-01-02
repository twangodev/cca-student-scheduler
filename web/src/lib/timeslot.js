export function hhmmToHHMMSS(obj, amOrPm) {
	if (amOrPm === 'pm') obj.hours = String(parseInt(obj.hours) + 12);
	const hours = obj.hours.padStart(2, '0');
	const minutes = obj.minutes.padStart(2, '0');
	return `${hours}:${minutes}:00`;
}
