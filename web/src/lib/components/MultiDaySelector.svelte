<script>
	export let label = '';
	export let invalid = false;
	export let invalidText = '';

	export let daysSelected = [];

	function selectedDay(daysSelected, day) {
		return daysSelected.includes(day) ? 'selected' : '';
	}

	function onDayClicked(day) {
		if (daysSelected.includes(day)) {
			daysSelected = daysSelected.filter((d) => d !== day);
		} else {
			daysSelected = [...daysSelected, day];
		}
	}
</script>

<div>
	<span class="bx--label">{label}</span>
	<div class="days">
		<button
			class="day {selectedDay(daysSelected, 'SUNDAY')}"
			on:click={() => onDayClicked('SUNDAY')}>Su</button
		>
		<button
			class="day {selectedDay(daysSelected, 'MONDAY')}"
			on:click={() => onDayClicked('MONDAY')}>M</button
		>
		<button
			class="day {selectedDay(daysSelected, 'TUESDAY')}"
			on:click={() => onDayClicked('TUESDAY')}>Tu</button
		>
		<button
			class="day {selectedDay(daysSelected, 'WEDNESDAY')}"
			on:click={() => onDayClicked('WEDNESDAY')}>W</button
		>
		<button
			class="day {selectedDay(daysSelected, 'THURSDAY')}"
			on:click={() => onDayClicked('THURSDAY')}>Th</button
		>
		<button
			class="day {selectedDay(daysSelected, 'FRIDAY')}"
			on:click={() => onDayClicked('FRIDAY')}>F</button
		>
		<button
			class="day {selectedDay(daysSelected, 'SATURDAY')}"
			on:click={() => onDayClicked('SATURDAY')}>Sa</button
		>
	</div>
	{#if invalid}
		<span class="bx--label" style="color: #da1e28">{invalidText}</span>
	{/if}
</div>

<style>
	.days {
		display: flex;
		flex-direction: row;
		margin-bottom: 8px;
	}

	.day {
		width: 40px;
		height: 40px;
		border-radius: 50%;
		border: 1px solid #161616;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		user-select: none;
		margin: 0 8px;
	}

	.day:first-child {
		margin-left: 0;
	}

	.day:last-child {
		margin-right: 0;
	}

	.day.selected {
		border: 2px solid #161616;
		background-color: #e0e0e0;
	}
</style>
