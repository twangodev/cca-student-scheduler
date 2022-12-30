<script>
	import {
		AspectRatio,
		Breadcrumb,
		ClickableTile,
		Column,
		DataTableSkeleton,
		Grid,
		Row,
		SkeletonPlaceholder,
		SkeletonText,
		Tag,
		Tile,
		Tooltip
	} from 'carbon-components-svelte';
	import { GET } from '$lib/http.js';
	import DaySelector from "$lib/components/MultiDaySelector.svelte";
</script>

<Grid>
	<Row>
		<Column>
			<h2>Overview</h2>
		</Column>
	</Row>

</Grid>
