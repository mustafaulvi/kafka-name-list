<template>
  <div class="card">
    <div class="card-header">
      Add New User
    </div>
    <div class="card-body">
      <form @submit.prevent="handleSubmit">
        <div class="mb-3">
          <label for="firstName" class="form-label">First Name</label>
          <input
            type="text"
            class="form-control"
            id="firstName"
            v-model="user.firstName"
            required
          >
        </div>
        <div class="mb-3">
          <label for="lastName" class="form-label">Last Name</label>
          <input
            type="text"
            class="form-control"
            id="lastName"
            v-model="user.lastName"
            required
          >
        </div>
        <button type="submit" class="btn btn-primary">Add User</button>
      </form>
    </div>
  </div>
</template>

<script>
import { createUser } from '../services/userService'

export default {
  name: 'UserForm',
  data() {
    return {
      user: {
        firstName: '',
        lastName: ''
      }
    }
  },
  methods: {
    async handleSubmit() {
      try {
        await createUser(this.user)
        this.$emit('user-added')
        this.user = { firstName: '', lastName: '' }
      } catch (error) {
        console.error('Error creating user:', error)
      }
    }
  }
}
</script> 