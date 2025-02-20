<template>
  <div class="container mt-4">
    <h1>User Management System</h1>
    <div class="row mt-4">
      <div class="col-md-4">
        <UserForm @user-added="refreshUsers" />
      </div>
      <div class="col-md-8">
        <UserList :users="users" />
      </div>
    </div>
  </div>
</template>

<script>
import UserForm from './components/UserForm.vue'
import UserList from './components/UserList.vue'
import { fetchUsers } from './services/userService'

export default {
  name: 'App',
  components: {
    UserForm,
    UserList
  },
  data() {
    return {
      users: []
    }
  },
  methods: {
    async refreshUsers() {
      try {
        this.users = await fetchUsers()
      } catch (error) {
        console.error('Error fetching users:', error)
      }
    }
  },
  mounted() {
    this.refreshUsers()
  }
}
</script>

<style>
@import 'bootstrap/dist/css/bootstrap.min.css';
</style> 