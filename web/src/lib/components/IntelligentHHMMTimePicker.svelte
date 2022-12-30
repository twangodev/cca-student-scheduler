<script>

  import { TimePicker } from "carbon-components-svelte";

  export let labelText = "";
  export let placeholder = "";
  export let value = "";

  export let invalid = false
  export let invalidText = ""
  export let onChange = () => {}

  export let interpretedHHMM;
  $: interpretedHHMM = {
    hours: tryToExtractHour(value),
    minutes: tryToExtractMinutes(value),
  }

  function tryToExtractHour(value) {
    invalid = false
    value = value.replace(":", "");
    if (value.length <= 2) {return null;}
    let interpreted;
    if (value.length === 3) {
      interpreted =  value.substring(0, 1)
    } else {
      interpreted =  value.substring(0, 2)
    }

    if (interpreted > 12) {
      invalid = true
      invalidText = "Hours cannot be greater than 12"
      return null;
    }
    if (interpreted === 0) return null
    return interpreted;
  }

  function tryToExtractMinutes(value) {
    invalid = false
    value = value.replace(":", "");
    if (value.length <= 2) {return null;}
    let interpreted;
    if (value.length === 3) {
      interpreted = value.substring(1, 3)
    } else {
      interpreted =  value.substring(2, 4)
    }
    if (interpreted > 59) {
      return null;
    }
    return interpreted;
  }

</script>

<TimePicker
  {labelText}
  {placeholder}
  bind:value={value}
  {invalid}
  {invalidText}
  on:input={() => {
    if (interpretedHHMM.hours && interpretedHHMM.minutes) {
      value = `${interpretedHHMM.hours}:${interpretedHHMM.minutes}`;
    }
    invalid = false
    invalidText = ""
    onChange();
  }}

>
  <slot />
</TimePicker>