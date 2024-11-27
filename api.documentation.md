# Document Management API

## Endpoints

### `/api/documents`

#### **GET /api/documents**
Fetch a list of all available documents.

- **Authorization**: Requires authentication.
- **Response**:
  - Status: `200 OK`
  - Body:  
    ```json
    {
      "statusCode": 200,
      "message": "Documents fetched successfully",
      "data": [
        {
          "id": 1,
          "title": "Sample Document",
          "description": "Description here",
          ...
        }
      ]
    }
    ```

---

#### **GET /api/documents/{id}**
Fetch a single document by its unique ID.

- **Authorization**: Requires authentication.
- **Path Parameter**:
  - `id`: (Long) The ID of the document.
- **Response**:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "statusCode": 200,
      "message": "Document fetched successfully",
      "data": {
        "id": 1,
        "title": "Sample Document",
        "description": "Description here",
        ...
      }
    }
    ```

---

#### **POST /api/documents**
Upload and create a new document.

- **Authorization**: Requires authentication.
- **Request Body**:
  - `DocumentRequest` (JSON or Form-Data):
    - `title`: (String) Title of the document.
    - `description`: (String) Description of the document.
    - Optional file uploads:
      - `docFile`: Multipart file (document).
      - `videoFile`: Multipart file (video).
      - `imageFile`: Multipart file (image).
- **Response**:
  - Status: `201 Created`
  - Body:
    ```json
    {
      "statusCode": 201,
      "message": "Document created successfully",
      "data": {
        "id": 1,
        "title": "Sample Document",
        ...
      }
    }
    ```

---

#### **PUT /api/documents/{id}**
Update an existing document by its unique ID.

- **Authorization**: Requires authentication.
- **Path Parameter**:
  - `id`: (Long) The ID of the document.
- **Request Body**:
  - `DocumentRequest` (JSON):
    - `title`: (String) Updated title.
    - `description`: (String) Updated description.
- **Response**:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "statusCode": 200,
      "message": "Document updated successfully",
      "data": {
        "id": 1,
        "title": "Updated Document",
        ...
      }
    }
    ```

---

#### **DELETE /api/documents/{id}**
Delete an existing document by its unique ID.

- **Authorization**: Requires authentication.
- **Path Parameter**:
  - `id`: (Long) The ID of the document.
- **Response**:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "statusCode": 200,
      "message": "Document deleted successfully",
      "data": null
    }
    ```

---

### `/auth`

#### **POST /auth/login**
Authenticate a user and retrieve a JWT.

- **Request Body**:
  - `LoginRequest` (JSON):
    - `username`: (String) The username.
    - `password`: (String) The password.
- **Response**:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "token": "jwt-token",
      "refreshToken": "refresh-jwt-token",
      "username": "username",
      "email": "user@example.com",
      "roles": ["user"]
    }
    ```

---

#### **POST /auth/signup**
Register a new user.

- **Request Body**:
  - `SignUpRequest` (JSON):
    - `username`: (String) The username.
    - `password`: (String) The password.
    - `email`: (String) The email.
    - `name`: (String) The user's name.
- **Response**:
  - Status: `201 Created`
  - Body:
    ```json
    {
      "id": "username",
      "email": "user@example.com",
      "name": "User Name",
      "roles": ["user"]
    }
    ```

---

#### **POST /auth/refreshToken**
Refresh an existing JWT.

- **Request Body**:
  - `RefreshTokenRequest` (JSON):
    - `refreshToken`: (String) The refresh token.
- **Response**:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "token": "new-jwt-token",
      "refreshToken": "new-refresh-token",
      "username": "username",
      "email": "user@example.com",
      "roles": ["user"]
    }
    ```

---

