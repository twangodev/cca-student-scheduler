<script>
	import {
		Button,
		Column,
		ComposedModal,
		ContentSwitcher,
		DataTable,
		DataTableSkeleton,
		Grid,
		Modal,
		ModalBody,
		ModalFooter,
		ModalHeader,
		OverflowMenu,
		OverflowMenuItem,
		ProgressBar,
		Row,
		SelectItem,
		Switch,
		TextArea,
		TextInput,
		TimePickerSelect,
		Toolbar,
		ToolbarContent,
		ToolbarMenu,
		ToolbarMenuItem,
		ToolbarSearch
	} from 'carbon-components-svelte';
	import { DELETE, GET, POST } from '$lib/http.js';
	import { onMount } from 'svelte';
	import { StatusCodes } from 'http-status-codes';
	import DaySelector from '$lib/components/MultiDaySelector.svelte';
	import IntelligentHHMMTimePicker from '$lib/components/IntelligentHHMMTimePicker.svelte';
	import { hhmmToHHMMSS } from '$lib/timeslot.js';

	const headers = [
		{ key: 'name', value: 'Name' },
		{ key: 'description', value: 'Description' },
		{ key: 'startTime', value: 'Start Time' },
		{ key: 'endTime', value: 'End Time' },
		{ key: 'actions', value: 'Actions', empty: true }
	];

	const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
	const date = new Date();
	let selectedIndex = date.getDay();
	let selectedDay = days[selectedIndex];
	$: selectedDay = days[selectedIndex];

	let amOrPm = 'am';
	if (date.getHours() > 12) {
		amOrPm = 'pm';
	}

	let timeslots = [];
	let loading = true;

	async function updateTimeslots() {
		loading = true;
		const response = await GET(`/timeslots/${selectedDay.toLowerCase()}`);
		if (response.status === StatusCodes.OK) {
			timeslots = await response.json();
		}
		loading = false;
	}

	let createTimeslotModalBinding = false;
	let name = '';
	let showNameRequired = false;
	let description = '';
	let daysOfWeek = [selectedDay.toUpperCase()];
	$: daysOfWeekInvalid = daysOfWeek.length === 0;
	let startTime = {
		hours: null,
		minutes: null
	};
	let startTimeValue = '';
	let startTimeSuffix = amOrPm;
	let startTimeInvalid = false;
	let startTimeError = '';
	let endTime = {
		hours: null,
		minutes: null
	};
	let endTimeValue = '';
	let endTimeInvalid = false;
	let endTimeError = '';
	let endTimeSuffix = amOrPm;
	let conflictMessage = '';
	let createProgress = 0;

	function showCreateTimeslotModal() {
		createTimeslotModalBinding = true;
		name = '';
		showNameRequired = false;
		description = '';
		daysOfWeek = [selectedDay.toUpperCase()];
		startTime = {
			hours: null,
			minutes: null
		};
		startTimeValue = '';
		startTimeSuffix = amOrPm;
		startTimeInvalid = false;
		startTimeError = '';
		endTime = {
			hours: null,
			minutes: null
		};
		endTimeValue = '';
		endTimeInvalid = false;
		endTimeSuffix = amOrPm;
		conflictMessage = '';
		createProgress = 0;
	}

	async function createTimeslot() {
		let returnFromInvalidInput = false;
		if (name === '') {
			showNameRequired = true;
			returnFromInvalidInput = true;
		}

		if (startTime.hours == null || startTime.minutes == null) {
			startTimeInvalid = true;
			startTimeError = 'Malformed start time';
			returnFromInvalidInput = true;
		}

		if (endTime.hours == null) {
			endTimeInvalid = true;
			endTimeError = 'Malformed end time';
			returnFromInvalidInput = true;
		}

		if (daysOfWeekInvalid) returnFromInvalidInput = true;

		if (returnFromInvalidInput) return;

		createProgress = undefined;

		const data = {
			name: name,
			description: description,
			daysOfWeek: daysOfWeek,
			startTime: hhmmToHHMMSS(startTime, startTimeSuffix),
			endTime: hhmmToHHMMSS(endTime, endTimeSuffix)
		};

		const response = await POST('/timeslots', data);
		if (response.status === StatusCodes.CREATED) {
			createTimeslotModalBinding = false;
			await updateTimeslots();
		} else {
			createProgress = 0;
			let errors = await response.json();
			errors.forEach((e) => {
				let conflictString = '';
				switch (e.title) {
					case 'START_TIME':
						startTimeInvalid = true;
						startTimeError = e.description;
						break;
					case 'CONFLICT':
						e.data.forEach((c) => {
							conflictString += `${c.name} from ${c.startTime} to ${c.endTime} on ${c.dayOfWeek}, `;
						});
						break;
				}

				if (conflictString !== '') {
					conflictMessage = `Conflicts with: ${conflictString.slice(0, -2)}`;
				}
			});
		}

		createProgress = 100;
	}

	let deleteModalBinding = false;
	let selectedTimeslotDelete = null;

	async function deleteTimeslot() {
		await DELETE('/timeslots/' + selectedTimeslotDelete.id);
		deleteModalBinding = false;
		selectedTimeslotDelete = null;
		await updateTimeslots();
	}

	function cancelDeleteRoom() {
		deleteModalBinding = false;
		selectedTimeslotDelete = null;
	}

	onMount(async () => {
		await updateTimeslots();
	});
</script>

<Grid>
	<ComposedModal bind:open={createTimeslotModalBinding} on:submit={createTimeslot} size="lg">
		<ModalHeader label="Timeslot" title="Create Timeslot" />
		<ModalBody hasForm>
			<TextInput
				labelText="Name"
				placeholder="Enter timeslot name or identifier"
				helperText="Example: A Period, Lunch, etc."
				invalidText="Name is required"
				invalid={showNameRequired}
				on:input={(e) => {
					const value = e.detail;
					showNameRequired = value === '';
				}}
				bind:value={name}
			/>
			<TextArea
				labelText="Description"
				placeholder="Enter optional timeslot description"
				helperText="Example: A Period is the first period of the day."
				bind:value={description}
			/>
			<DaySelector
				label="Effective Days of Week"
				bind:daysSelected={daysOfWeek}
				bind:invalid={daysOfWeekInvalid}
				invalidText="At least one day must be selected"
			/>
			<Row>
				<Column>
					<IntelligentHHMMTimePicker
						labelText="Start Time"
						placeholder="hh:mm"
						bind:interpretedHHMM={startTime}
						bind:value={startTimeValue}
						bind:invalid={startTimeInvalid}
						bind:invalidText={startTimeError}
						onChange={() => {
							conflictMessage = '';
						}}
					>
						<TimePickerSelect bind:value={startTimeSuffix}>
							<SelectItem value="am" text="AM" />
							<SelectItem value="pm" text="PM" />
						</TimePickerSelect>
					</IntelligentHHMMTimePicker>
				</Column>
				<Column>
					<IntelligentHHMMTimePicker
						labelText="End Time"
						placeholder="hh:mm"
						bind:interpretedHHMM={endTime}
						bind:value={endTimeValue}
						bind:invalid={endTimeInvalid}
						bind:invalidText={endTimeError}
						onChange={() => {
							conflictMessage = '';
						}}
					>
						<TimePickerSelect bind:value={endTimeSuffix}>
							<SelectItem value="am" text="AM" />
							<SelectItem value="pm" text="PM" />
						</TimePickerSelect>
					</IntelligentHHMMTimePicker>
				</Column>
			</Row>
			<span class="bx--label" style="color: #da1e28">{conflictMessage}</span>
		</ModalBody>
		<ProgressBar size="sm" value={createProgress} />
		<ModalFooter primaryButtonText="Create" secondaryButtonText="Cancel" />
	</ComposedModal>

	<Modal
		danger
		bind:open={deleteModalBinding}
		modalHeading="Delete Timeslot: {selectedTimeslotDelete?.name}"
		primaryButtonText="Delete"
		secondaryButtonText="Cancel"
		on:submit={deleteTimeslot}
		on:click:button--secondary={cancelDeleteRoom}
	>
		<p>This is a permanent action and cannot be undone</p>
	</Modal>

	<ContentSwitcher bind:selectedIndex on:change={updateTimeslots}>
		<Switch text="Sunday" />
		<Switch text="Monday" />
		<Switch text="Tuesday" />
		<Switch text="Wednesday" />
		<Switch text="Thursday" />
		<Switch text="Friday" />
		<Switch text="Saturday" />
	</ContentSwitcher>

	{#if loading}
		<DataTableSkeleton {headers} />
	{:else}
		<DataTable
			size="short"
			title="Timeslots Management - {selectedDay}"
			description="Your organizations timeslots for lessons"
			{headers}
			rows={timeslots}
		>
			<svelte:fragment slot="cell" let:cell let:row>
				{#if cell.key === 'actions'}
					<OverflowMenu flipped>
						<OverflowMenuItem
							danger
							text="Delete"
							on:click={() => {
								selectedTimeslotDelete = row;
								deleteModalBinding = true;
							}}
						/>
					</OverflowMenu>
				{:else if !cell.value}
					N/A
				{:else}
					{cell.value}
				{/if}
			</svelte:fragment>
			<Toolbar size="sm">
				<ToolbarContent>
					<ToolbarSearch />
					<ToolbarMenu>
						<!-- TODO Implement Delete All Function-->
						<ToolbarMenuItem danger>Delete All</ToolbarMenuItem>
					</ToolbarMenu>
					<Button on:click={showCreateTimeslotModal}>Create Timeslot</Button>
				</ToolbarContent>
			</Toolbar>
		</DataTable>
	{/if}
</Grid>
