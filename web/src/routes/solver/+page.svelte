<script>
	import {
		AspectRatio,
		ClickableTile,
		Column,
		Grid,
		Row,
		SkeletonPlaceholder,
		Tag
	} from 'carbon-components-svelte';
	import { onDestroy, onMount } from 'svelte';
	import { GET } from '$lib/http.js';
	import NumericalBox from '$lib/components/NumericalBox.svelte';
	import NumericalBoxContainer from '$lib/components/NumericalBoxContainer.svelte';
	import DaySelector from "$lib/components/MultiDaySelector.svelte";

	let status = {
		color: null,
		text: null
	};

	let score = [null, null];

	async function updateTimeTable() {
		const response = await GET('/time-table');
		const data = await response.json();

		if (data.solverStatus === 'SOLVING_ACTIVE') {
			status.color = 'magenta';
			status.text = 'Solving';
		} else if (data.solverStatus === 'NOT_SOLVING') {
			status.color = 'green';
			status.text = 'Not solving';
		} else {
			status.color = 'outline';
			status.text = 'Unknown State';
		}

		score = data.hardSoftScore;
	}

	let updateInterval = undefined;

	onMount(async () => {
		await updateTimeTable();
		updateInterval = setInterval(updateTimeTable, 1000);
	});

	onDestroy(() => {
		clearInterval(updateInterval);
	});
</script>

<head>
	<title>Constraint Solver</title>
</head>

<Grid>
	<Row>
		<Column>
			<h2>Constraint Solver</h2>
		</Column>
	</Row>

	{#if status.color == null || status.text == null}
		<Tag skeleton />
	{:else}
		<Tag type={status.color}>Status: {status.text}</Tag>
	{/if}

	{#if score[0] == null || score[1] == null}
		<Tag skeleton />
	{:else}
		<Tag type="outline">Hard: {score[0]} Soft: {score[1]}</Tag>
	{/if}

	<Row>
		<Column>
			<NumericalBoxContainer>
				<NumericalBox data="0" caption="Lessons" />
				<NumericalBox data="0" caption="Rooms" />
				<NumericalBox data="0" caption="Teachers" />
				<NumericalBox data="0" caption="Timeslots" />
			</NumericalBoxContainer>
		</Column>
	</Row>
</Grid>
