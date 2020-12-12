const API_BASE_HOST = "https://localhost:1116"
// const API_BASE_HOST = "https://ufms.ordinaryroad.top:1116"

const API_BASE_URL = API_BASE_HOST + "/api"

const PRODUCT_BASE_URL = API_BASE_URL + "/product"
const FEEDBACK_BASE_URL = API_BASE_URL + "/feedback"
const CAPTCHA_BASE_URL = API_BASE_URL + "/captcha"
const USER_BASE_URL = API_BASE_URL + "/user"
const FILE_BASE_URL = API_BASE_URL + "/file"
const STATISTIC_BASE_URL = API_BASE_URL + "/statistic"
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
    FIND: FEEDBACK_BASE_URL + FIND + "?id=",
    FIND_ALL_BY_FOREIGN_KEY: FEEDBACK_BASE_URL + FIND_ALL_BY_FOREIGN_KEY + "?productId=",
    FIND_ALL_FEEDBACKS: FEEDBACK_BASE_URL + "/findAllFeedbacks?productId=",
    FIND_ALL_FEEDBACK_REPLIES: FEEDBACK_BASE_URL + "/findAllFeedbackReplies?productId=",
    FIND_ALL_USER_FEEDBACK_AND_REPLY: FEEDBACK_BASE_URL + "/findAllUserFeedbackAndReply?productId=",
    INSERT: FEEDBACK_BASE_URL + INSERT,
    DELETE: FEEDBACK_BASE_URL + DELETE,
    UPDATE: FEEDBACK_BASE_URL + UPDATE,
    LIKE: FEEDBACK_BASE_URL + "/like?feedbackId=",
    TOGGLE_IS_PROPERTY: FEEDBACK_BASE_URL + "/toggleIsProperty?feedbackId=",
}

const CAPTCHA = {
    GENERATE: CAPTCHA_BASE_URL + "/generate",
    GENERATE_EMAIL_CODE: CAPTCHA_BASE_URL + "/generateEmailCode",
}

const USER = {
    LOGIN: USER_BASE_URL + "/login",
    LOGOUT: USER_BASE_URL + "/logout",
    REGISTER: USER_BASE_URL + "/register",
    RESET_PASSWORD: USER_BASE_URL + "/resetPassword",
    UPDATE: USER_BASE_URL + "/update",
    CHECK_USERNAME: USER_BASE_URL + "/checkUsername",
    GENERATE: USER_BASE_URL + "/generate",
}

const FILE = {
    UPLOAD: FILE_BASE_URL + "/upload",
    DOWNLOAD: FILE_BASE_URL + "/download"
}

const STATISTIC = {
    QUERY_FEEDBACK_COUNT: STATISTIC_BASE_URL + "/queryFeedbackCount/",
    QUERY_FEEDBACK_REPLY_COUNT: STATISTIC_BASE_URL + "/queryFeedbackReplyCount/",
    QUERY_RECOMMEND_FEEDBACK_COUNT: STATISTIC_BASE_URL + "/queryRecommendFeedbackCount/",
    QUERY_ADMIN_FEEDBACK_REPLY_COUNT: STATISTIC_BASE_URL + "/queryAdminFeedbackReplyCount/",
    QUERY_SEVEN_DAYS_FEEDBACK_AND_ADMIN_REPLY: STATISTIC_BASE_URL + "/querySevenDaysFeedbackAndAdminReply/",
    QUERY_FEEDBACK_WORD_FREQUENCY: STATISTIC_BASE_URL + "/queryFeedbackWordFrequency/",
}

export const API = {
    API_BASE_HOST: API_BASE_HOST,
    PRODUCT: PRODUCT,
    FEEDBACK: FEEDBACK,
    CAPTCHA: CAPTCHA,
    USER: USER,
    FILE: FILE,
    STATISTIC: STATISTIC,
}

export default {
    API: API
}

window.API = API