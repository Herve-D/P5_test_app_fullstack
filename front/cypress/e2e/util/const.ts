const admin = {
    id: 1,
    username: 'admin',
    firstName: 'first',
    lastName: 'last',
    admin: true
}

const user = {
    id: 1,
    email: 'user@mail.com',
    lastName: 'last',
    firstName: 'first',
    admin: false,
    createdAt: '2024-02-16',
    updatedAt: '2024-02-16',
}

const sessionOne = {
    id: 1,
    name: 'Session One',
    description: 'One',
    date: '2024-02-16',
    teacher_id: 1,
}

const sessionTwo = {
    id: 2,
    name: 'Session Two',
    description: 'Two',
    date: '2024-02-16',
    teacher_id: 1
}

const teacher = {
    id: 1,
    firstName: 'first',
    lastName: 'last',
}
export { admin, user, sessionOne, sessionTwo, teacher }