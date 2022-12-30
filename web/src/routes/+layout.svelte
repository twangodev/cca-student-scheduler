<script>
	import 'carbon-components-svelte/css/white.css';
	import {
		Content,
		Header,
		HeaderNav,
		HeaderNavItem,
		HeaderNavMenu,
		HeaderSearch,
		HeaderUtilities,
		SkipToContent
	} from 'carbon-components-svelte';

	let isSideNavOpen = false;
</script>

<Header company="CCA" platformName="Student Scheduler" bind:isSideNavOpen>
	<svelte:fragment slot="skip-to-content">
		<SkipToContent />
	</svelte:fragment>

	<HeaderNav>
		<HeaderNavItem href="/" text="Dashboard" />
		<HeaderNavItem href="/solver" text="Solver" />
		<HeaderNavMenu text="Manage">
			<HeaderNavItem href="/management/lessons" text="Lessons" />
			<HeaderNavItem href="/management/rooms" text="Rooms" />
			<HeaderNavItem href="/management/teachers" text="Teachers" />
			<HeaderNavItem href="/management/timeslots" text="Timeslots" />
		</HeaderNavMenu>
	</HeaderNav>
	<HeaderUtilities>
		<HeaderSearch />
	</HeaderUtilities>
</Header>

<Content>
	<slot />
</Content>
