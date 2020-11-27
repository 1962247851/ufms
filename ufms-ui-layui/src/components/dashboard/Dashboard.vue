<template>
  <div>
    <router-view name="drawer"/>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: "Dashboard",
  components: {},
  data: () => ({
    admins: [
      ['Management', 'people_outline'],
      ['Settings', 'mdi-settings'],
    ],
    cruds: [
      ['Create', 'mdi-plus'],
      ['Read', 'insert_drive_file'],
      ['Update', 'mdi-update'],
      ['Delete', 'mdi-delete'],
    ],
  }),
  methods: {
    onListItemClick(crud) {
      alert(crud)
    }
  }
}
</script>

<style scoped>

</style>