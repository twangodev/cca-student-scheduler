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
		NumberInput,
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
	import { StatusCodes } from 'http-status-codes';
	import { DELETE, GET, POST } from '$lib/http.js';

	const headers = [
		{ key: 'name', value: 'Name' },
		{ key: 'description', value: 'Description' },
		{ key: 'capacity', value: 'Capacity' },
		{ key: 'actions', value: 'Actions', empty: true }
	];

	let rooms = [];

	async function updateRooms() {
		const response = await GET('/rooms');
		if (response.status === StatusCodes.OK) {
			rooms = await response.json();
		}
	}

	let createNewRoomModalBinding = false;
	let showNameRequired = false;
	let name = '';
	let description = '';
	let capacity = 0;
	let createProgress = 0;

	function showCreateRoomModal() {
		createNewRoomModalBinding = true;
		showNameRequired = false;
		name = '';
		description = '';
		capacity = 0;
		createProgress = 0;
	}

	async function createNewRoom() {
		if (name === '') {
			showNameRequired = true;
			return;
		}

		createProgress = undefined;

		if (capacity === 0) capacity = null;

		const data = {
			name: name,
			description: description,
			capacity: capacity
		};
		const response = await POST('/rooms', data);
		if (response.status === StatusCodes.CREATED) {
			createNewRoomModalBinding = false;
			await updateRooms();
		} else {
			// TODO handle error with inline notification
		}
		createProgress = 100;
	}

	let deleteModalBinding = false;
	let selectedRoomDelete = null;

	async function deleteRoom() {
		await DELETE('/rooms/' + selectedRoomDelete.id);
		deleteModalBinding = false;
		selectedRoomDelete = null;
		await updateRooms();
	}

	function cancelDeleteRoom() {
		deleteModalBinding = false;
		selectedRoomDelete = null;
	}
</script>

<Grid>
	<ComposedModal bind:open={createNewRoomModalBinding} on:submit={createNewRoom}>
		<ModalHeader label="Room" title="Create Room" />
		<ModalBody hasForm>
			<TextInput
				labelText="Name"
				placeholder="Enter room name or identifier"
				helperText="Example: R100, R101, etc."
				invalidText="This field is required."
				invalid={showNameRequired}
				on:input={(e) => {
					const value = e.detail;
					showNameRequired = value === '';
				}}
				bind:value={name}
			/>
			<TextArea
				labelText="Description"
				placeholder="Enter optional room description"
				helperText="Example: Art room, Makerspace, etc."
				bind:value={description}
			/>
			<NumberInput
				allowEmpty
				label="Capacity"
				placeholder="Enter room capacity"
				helperText="Leave empty or 0 if irrelevant"
				bind:value={capacity}
			/>
		</ModalBody>
		<ProgressBar size="sm" value={createProgress} />
		<ModalFooter primaryButtonText="Create" secondaryButtonText="Cancel" />
	</ComposedModal>

	<Modal
		danger
		bind:open={deleteModalBinding}
		modalHeading="Delete Room {selectedRoomDelete?.name}"
		primaryButtonText="Delete"
		secondaryButtonText="Cancel"
		on:submit={deleteRoom}
		on:click:button--secondary={cancelDeleteRoom}
	>
		<p>This is a permanent action and cannot be undone</p>
	</Modal>

	{#await updateRooms()}
		<DataTableSkeleton {headers} />
	{:then _}
		<DataTable
			size="short"
			title="Room Management"
			description="Your organization's rooms for classes, meetings, and more."
			{headers}
			rows={rooms}
		>
			<svelte:fragment slot="cell" let:cell let:row>
				{#if cell.key === 'actions'}
					<OverflowMenu flipped>
						<OverflowMenuItem
							danger
							text="Delete"
							on:click={() => {
								selectedRoomDelete = row;
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
					<Button on:click={showCreateRoomModal}>Create Room</Button>
				</ToolbarContent>
			</Toolbar>
		</DataTable>
	{/await}
</Grid>
