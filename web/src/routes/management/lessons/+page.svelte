<script>
	import {
		Button,
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
		TextArea,
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
		{ key: 'name', value: 'Name' },
		{ key: 'description', value: 'Description' },
		{ key: 'category', value: 'Category' },
		{ key: 'teacher', value: 'Teacher' },
		{ key: 'actions', empty: true }
	];

	let lessons = [];

	async function updateLessons() {
		const response = await GET('/lessons');
		if (response.status === StatusCodes.OK) {
			lessons = await response.json();
		}
	}

	let createNewLessonModalBinding = false;
	let showNameRequired = false;
	let name = '';
	let description = '';
	let category = '';
	let teacher = '';
	let createProgress = 0;

	function showCreateLessonModal() {
		createNewLessonModalBinding = true;
		showNameRequired = false;
		name = '';
		description = '';
		category = '';
		teacher = '';
		createProgress = 0;
	}

	async function createLesson() {
		if (name === '') {
			showNameRequired = true;
			return;
		}

		createProgress = undefined;

		const data = {
			name: name,
			description: description,
			category: category,
			teacher: teacher
		};

		const response = await POST('/lessons', data);
		if (response.status === StatusCodes.CREATED) {
			createNewLessonModalBinding = false;
			await updateLessons();
		} else {
			// TODO handle error with inline notification
		}
		createProgress = 100;
	}

	let deleteModalBinding = false;
	let selectedLessonDelete = null;

	async function deleteLesson() {
		await DELETE('/lessons/' + selectedLessonDelete.id);
		deleteModalBinding = false;
		selectedLessonDelete = null;
		await updateLessons();
	}

	function cancelDeleteLesson() {
		deleteModalBinding = false;
		selectedLessonDelete = null;
	}
</script>

<Grid>
	<ComposedModal bind:open={createNewLessonModalBinding} on:submit={createLesson}>
		<ModalHeader label="Lesson" title="Create Lesson" />
		<ModalBody hasForm>
			<TextInput
				labelText="Name"
				placeholder="Enter lesson name or identifier"
				helperText="Example: 9th Grade History, PreCalculus Honors, CS61A, etc."
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
				placeholder="Enter optional lesson description"
				helperText="Example: PreCalculus Honors is an advanced course for students who have completed Algebra 2"
				bind:value={description}
			/>
			<TextInput
				labelText="Category"
				placeholder="Enter optional lesson category"
				helperText="Example: Math, Science, History, etc."
				bind:value={category}
			/>
			<TextInput
				labelText="Teacher"
				placeholder="Enter optional teacher name"
				helperText="Example: Mr. Smith, Mrs. Jones, etc."
				bind:value={teacher}
			/>
		</ModalBody>
		<ProgressBar size="sm" value={createProgress} />
		<ModalFooter primaryButtonText="Create" secondaryButtonText="Cancel" />
	</ComposedModal>

	<Modal
		danger
		bind:open={deleteModalBinding}
		modalHeading="Delete Room {selectedLessonDelete?.name}"
		primaryButtonText="Delete"
		secondaryButtonText="Cancel"
		on:submit={deleteLesson}
		on:click:button--secondary={cancelDeleteLesson}
	>
		<p>This is a permanent action and cannot be undone</p>
	</Modal>

	{#await updateLessons()}
		<DataTableSkeleton {headers} />
	{:then _}
		<DataTable
			size="short"
			title="Lessons Management"
			description="Your organization's lessons, activities, and more."
			{headers}
			rows={lessons}
		>
			<svelte:fragment slot="cell" let:cell let:row>
				{#if cell.key === 'actions'}
					<OverflowMenu flipped>
						<OverflowMenuItem
							danger
							text="Delete"
							on:click={() => {
								selectedLessonDelete = row;
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
						<ToolbarMenuItem>Import</ToolbarMenuItem>
						<ToolbarMenuItem danger>Delete All</ToolbarMenuItem>
					</ToolbarMenu>
					<Button on:click={showCreateLessonModal} kind="primary">Create Lesson</Button>
				</ToolbarContent>
			</Toolbar>
		</DataTable>
	{/await}
</Grid>
