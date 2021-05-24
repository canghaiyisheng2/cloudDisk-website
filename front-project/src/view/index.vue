<template>
    <div>
        <h2>首页</h2>
    </div>
</template>

<script>
export default {
    created() {
        document.title = "首页";
    },
}
</script>

<style>
    .day > h2 {
        color: red;
    }
    .night > h2 {
        columns: blue;
    }
</style>