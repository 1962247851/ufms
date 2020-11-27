<template>
  <div>
    <div>HOME</div>
    <div>{{ account.id }}</div>
    <div>{{ account.name }}</div>
  </div>
</template>

<script>

export default {
  name: "Home",
  data: () => ({
    account: {}
  }),
  created() {
    this.AXIOS.get("https://localhost/hello").then(value => {
      console.log(value)
      this.account = value.data;
    })
  }
}
</script>

<style scoped>

</style>