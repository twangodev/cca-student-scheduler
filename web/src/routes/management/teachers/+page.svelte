<script>
	import {
		Button,
		Column,
		ComposedModal,
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
		RadioButton,
		RadioButtonGroup,
		Row,
		TextInput,
		Toolbar,
		ToolbarContent,
		ToolbarMenu,
		ToolbarMenuItem,
		ToolbarSearch
	} from 'carbon-components-svelte';
	import { DELETE, GET, POST } from '$lib/http.js';
	import { StatusCodes } from 'http-status-codes';

	const headers = [
		{ key: 'prefix', value: 'Prefix' },
		{ key: 'name', value: 'Name' },
		{ key: 'email', value: 'Email' },
		{ key: 'gender', value: 'Gender' },
		{ key: 'actions', value: 'Actions', empty: true }
	];

	let teachers = [];

	async function updateTeachers() {
		const response = await GET('/teachers');
		if (response.status === StatusCodes.OK) {
			teachers = await response.json();
		}
	}

	let addNewTeacherModalBinding = false;
	let prefix = '';
	let name = '';
	let showNameRequired = false;
	let email = '';
	let gender = '';
	let addProgress = 0;

	function showAddTeacherModal() {
		addNewTeacherModalBinding = true;
		prefix = '';
		name = '';
		showNameRequired = false;
		email = '';
		gender = '';
		addProgress = 0;
	}

	async function addNewTeacher() {
		if (name === '') {
			showNameRequired = true;
			return;
		}

		addProgress = undefined;

		const data = {
			prefix: prefix,
			name: name,
			email: email,
			gender: gender
		};

		const response = await POST('/teachers', data);
		if (response.status === StatusCodes.CREATED) {
			addNewTeacherModalBinding = false;
			await updateTeachers();
		} else {
			// TODO
		}

		addProgress = 100;
	}

	let deleteModalBinding = false;
	let selectedTeacherDelete = null;

	async function deleteTeacher() {
		await DELETE('/teachers/' + selectedTeacherDelete.id);
		deleteModalBinding = false;
		selectedTeacherDelete = null;
		await updateTeachers();
	}

	function cancelDeleteTeacher() {
		deleteModalBinding = false;
		selectedTeacherDelete = null;
	}
</script>

<Grid>
	<ComposedModal bind:open={addNewTeacherModalBinding} on:submit={addNewTeacher}>
		<ModalHeader label="Teacher" title="Add Teacher" />
		<ModalBody>
			<Row>
				<Column sm={3} md={3} lg={3}>
					<TextInput
						labelText="Prefix"
						placeholder="Prefix"
						helperText="Mr., Mrs., Ms., Dr., etc."
						bind:value={prefix}
					/>
				</Column>
				<Column>
					<TextInput
						labelText="Name"
						placeholder="Full Name"
						helperText="Example: John Doe, Jane Doe, etc."
						invalidText="This field is required."
						invalid={showNameRequired}
						on:input={(e) => {
							const value = e.detail;
							showNameRequired = value === '';
						}}
						bind:value={name}
					/>
				</Column>
			</Row>
			<TextInput
				labelText="Email"
				placeholder="Enter optional email"
				helperText="Example: johndoe@example.com, janedoe@example.com, etc."
				bind:value={email}
			/>
			<RadioButtonGroup legendText="Gender" bind:selected={gender}>
				<RadioButton labelText="Male" value="MALE" />
				<RadioButton labelText="Female" value="FEMALE" />
				<RadioButton labelText="Other" value="OTHER" />
			</RadioButtonGroup>
		</ModalBody>
		<ProgressBar size="sm" value={addProgress} />
		<ModalFooter primaryButtonText="Create" secondaryButtonText="Cancel" />
	</ComposedModal>

	<Modal
		danger
		bind:open={deleteModalBinding}
		modalHeading="Delete {selectedTeacherDelete?.prefix} {selectedTeacherDelete?.name}"
		primaryButtonText="Delete"
		secondaryButtonText="Cancel"
		on:submit={deleteTeacher}
		on:click:button--secondary={cancelDeleteTeacher}
	>
		<p>This is a permanent action and cannot be undone</p>
	</Modal>

	{#await updateTeachers()}
		<DataTableSkeleton {headers} />
	{:then _}
		<DataTable
			size="short"
			title="Teacher Management"
			description="Your organization's teachers, instructors, and lecturers."
			{headers}
			rows={teachers}
		>
			<svelte:fragment slot="cell" let:cell let:row>
				{#if cell.key === 'actions'}
					<OverflowMenu flipped>
						<OverflowMenuItem
							danger
							text="Delete"
							on:click={() => {
								selectedTeacherDelete = row;
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
					<Button on:click={showAddTeacherModal}>Add Teacher</Button>
				</ToolbarContent>
			</Toolbar>
		</DataTable>
	{/await}
</Grid>
