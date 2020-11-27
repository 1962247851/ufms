const API_BASE_URL = "/api"

const PRODUCT_BASE_URL = API_BASE_URL + "/product"
const FEEDBACK_BASE_URL = API_BASE_URL + "/feedback"
const CAPTCHA_BASE_URL = API_BASE_URL + "/captcha"
const USER_BASE_URL = API_BASE_URL + "/user"
const FILE_BASE_URL = API_BASE_URL + "/file"
const LIST = "/list"
const DELETE = "/delete"
const FIND_ALL = "/findAll"
const FIND_ALL_BY_FOREIGN_KEY = "/findAllByForeignKey"
const FIND = "/find"
const INSERT = "/insert"
const UPDATE = "/update"

const PRODUCT = {
    LIST: PRODUCT_BASE_URL + LIST,
    FIND: PRODUCT_BASE_URL + FIND + "?id=",
    FIND_ALL: PRODUCT_BASE_URL + FIND_ALL,
    INSERT: PRODUCT_BASE_URL + INSERT,
    DELETE: PRODUCT_BASE_URL + DELETE,
    UPDATE: PRODUCT_BASE_URL + UPDATE,
}

const FEEDBACK = {
    FIND_ALL_BY_FOREIGN_KEY: FEEDBACK_BASE_URL + FIND_ALL_BY_FOREIGN_KEY + "?productId=",
    FIND_ALL_FEEDBACKS: FEEDBACK_BASE_URL + "/findAllFeedbacks?productId=",
    FIND_ALL_FEEDBACK_REPLIES: FEEDBACK_BASE_URL + "/findAllFeedbackReplies?productId=",
    FIND_ALL_USER_FEEDBACK_AND_REPLY: FEEDBACK_BASE_URL + "/findAllUserFeedbackAndReply?productId=",
    INSERT: FEEDBACK_BASE_URL + INSERT,
    DELETE: FEEDBACK_BASE_URL + DELETE,
    UPDATE: FEEDBACK_BASE_URL + UPDATE,
}

const CAPTCHA = {
    GENERATE: CAPTCHA_BASE_URL + "/generate"
}

const USER = {
    LOGIN: USER_BASE_URL + "/login",
    LOGOUT: USER_BASE_URL + "/logout"
}

const FILE = {
    UPLOAD: FILE_BASE_URL + "/upload",
    DOWNLOAD: FILE_BASE_URL + "/download"
}

export const API = {
    PRODUCT: PRODUCT,
    FEEDBACK: FEEDBACK,
    CAPTCHA: CAPTCHA,
    USER: USER,
    FILE: FILE,
}

export default {
    API: API
}

window.API = API