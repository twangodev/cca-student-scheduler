<script>
	export let data;
	export let caption;
</script>

<div class="numerical-box">
	<div class="line" />
	<div class="box" style="flex-direction: column;">
		<h2 class="data">{data}</h2>
		<span class="caption">{caption}</span>
	</div>
</div>

<style>
	.numerical-box {
		flex: 1;
		margin: 0 10px;
	}
	.numerical-box:first-child {
		margin-left: 0;
	}
	.numerical-box:last-child {
		margin-right: 0;
	}
	.line {
		background-color: #0f62fe;
		height: 4px;
	}
	.box {
		padding: 30px 50px 30px 30px;
		display: flex;
		transition: background-color 0.2s ease;
		background-color: #e0e0e0;
	}
	.box:hover {
		background-color: #a8a8a8;
	}
	.data {
		color: var(--cds-text-01);
		text-align: left;
	}
	.caption {
		font-size: smaller;
		color: var(--cds-text-03);
		text-align: left;
	}
</style>
