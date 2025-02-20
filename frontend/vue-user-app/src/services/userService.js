const API_URL = '/api/users'

export async function createUser(user) {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(user)
  })
  
  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || 'Failed to create user')
  }
}

export async function fetchUsers() {
  const response = await fetch(API_URL)
  
  if (!response.ok) {
    const error = await response.text()
    throw new Error(error || 'Failed to fetch users')
  }
  
  return response.json()
} 