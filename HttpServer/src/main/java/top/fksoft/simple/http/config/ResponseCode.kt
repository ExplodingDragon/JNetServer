package top.fksoft.simple.http.config

/**
 * @author ExplodingDragon
 * @version 1.0
 */


data class ResponseCode (val responseCode:Int,val codeMessage:String){

    override fun toString(): String {
        return "$responseCode $codeMessage"
    }

    companion object{
        /**
         * The response codes for HTTP, as of version 1.1.
         */

        // REMIND: do we want all these??
        // Others not here that we do want??

        /* 2XX: generally "OK" */
        /**
         * HTTP Status-Code 200: OK.
         */
        @JvmStatic
        val HTTP_OK = ResponseCode(200,"OK")

        /**
         * HTTP Status-Code 201: Created.
         */
        @JvmStatic
        val HTTP_CREATED = ResponseCode(201,"CREATED")

        /**
         * HTTP Status-Code 202: Accepted.
         */
        @JvmStatic
        val HTTP_ACCEPTED = ResponseCode(202,"ACCEPTED")

        /**
         * HTTP Status-Code 203: Non-Authoritative Information.
         */
        @JvmStatic
        val HTTP_NOT_AUTHORITATIVE = ResponseCode(203,"NOT AUTHORITATIVE")

        /**
         * HTTP Status-Code 204: No Content.
         */
        @JvmStatic
        val HTTP_NO_CONTENT =  ResponseCode(204,"NO CONTENT")

        /**
         * HTTP Status-Code 205: Reset Content.
         */
        @JvmStatic
        val HTTP_RESET = ResponseCode(205,"RESET")

        /**
         * HTTP Status-Code 206: Partial Content.
         */
        @JvmStatic
        val HTTP_PARTIAL = ResponseCode(206,"PARTIAL")

        /* 3XX: relocation/redirect */

        /**
         * HTTP Status-Code 300: Multiple Choices.
         */
        @JvmStatic
        val HTTP_MULT_CHOICE = ResponseCode(300,"MULT CHOICE")

        /**
         * HTTP Status-Code 301: Moved Permanently.
         */
        @JvmStatic
        val HTTP_MOVED_PERM = ResponseCode(301,"MOVED PERM")

        /**
         * HTTP Status-Code 302: Temporary Redirect.
         */
        @JvmStatic
        val HTTP_MOVED_TEMP = ResponseCode(302,"MOVED TEMP")

        /**
         * HTTP Status-Code 303: See Other.
         */
        @JvmStatic
        val HTTP_SEE_OTHER = ResponseCode(303,"SEE OTHER")

        /**
         * HTTP Status-Code 304: Not Modified.
         */
        @JvmStatic
        val HTTP_NOT_MODIFIED = ResponseCode(304,"NOT MODIFIED")

        /**
         * HTTP Status-Code 305: Use Proxy.
         */
        @JvmStatic
        val HTTP_USE_PROXY = ResponseCode(305,"USE PROXY")

        /* 4XX: client error */

        /**
         * HTTP Status-Code 400: Bad Request.
         */
        @JvmStatic
        val HTTP_BAD_REQUEST = ResponseCode(400,"BAD REQUEST")

        /**
         * HTTP Status-Code 401: Unauthorized.
         */
        @JvmStatic
        val HTTP_UNAUTHORIZED = ResponseCode(401,"UNAUTHORIZED")

        /**
         * HTTP Status-Code 402: Payment Required.
         */
        @JvmStatic
        val HTTP_PAYMENT_REQUIRED = ResponseCode(402,"PAYMENT REQUIRED")

        /**
         * HTTP Status-Code 403: Forbidden.
         */
        @JvmStatic
        val HTTP_FORBIDDEN = ResponseCode(403,"FORBIDDEN")

        /**
         * HTTP Status-Code 404: Not Found.
         */
        @JvmStatic
        val HTTP_NOT_FOUND = ResponseCode(404,"NOT FOUND")

        /**
         * HTTP Status-Code 405: Method Not Allowed.
         */
        @JvmStatic
        val HTTP_BAD_METHOD = ResponseCode(405,"BAD METHOD")

        /**
         * HTTP Status-Code 406: Not Acceptable.
         */
        @JvmStatic
        val HTTP_NOT_ACCEPTABLE = ResponseCode(406,"NOT ACCEPTABLE")

        /**
         * HTTP Status-Code 407: Proxy Authentication Required.
         */
        @JvmStatic
        val HTTP_PROXY_AUTH = ResponseCode(407,"PROXY AUTH")

        /**
         * HTTP Status-Code 408: Request Time-Out.
         */
        @JvmStatic
        val HTTP_CLIENT_TIMEOUT = ResponseCode(408,"CLIENT TIMEOUT")

        /**
         * HTTP Status-Code 409: Conflict.
         */
        @JvmStatic
        val HTTP_CONFLICT = ResponseCode(409,"CONFLICT")

        /**
         * HTTP Status-Code 410: Gone.
         */
        @JvmStatic
        val HTTP_GONE = ResponseCode(410,"GONE")

        /**
         * HTTP Status-Code 411: Length Required.
         */
        @JvmStatic
        val HTTP_LENGTH_REQUIRED = ResponseCode(411,"LENGTH REQUIRED")

        /**
         * HTTP Status-Code 412: Precondition Failed.
         */
        @JvmStatic
        val HTTP_PRECON_FAILED = ResponseCode(412,"PRECON FAILED")

        /**
         * HTTP Status-Code 413: Request Entity Too Large.
         */
        @JvmStatic
        val HTTP_ENTITY_TOO_LARGE = ResponseCode(413,"ENTITY TOO LARGE")

        /**
         * HTTP Status-Code 414: Request-URI Too Large.
         */
        @JvmStatic
        val HTTP_REQ_TOO_LONG = ResponseCode(414,"REQ TOO LONG")

        /**
         * HTTP Status-Code 415: Unsupported Media Type.
         */
        @JvmStatic
        val HTTP_UNSUPPORTED_TYPE = ResponseCode(415,"UNSUPPORTED TYPE")

        /**
         * HTTP Status-Code 426: Unsupported Media Type.
         *
         * The 426 (Upgrade Required) status code indicates that the simple
         * refuses to perform the request using the current protocol but might
         * be willing to do so after the client upgrades to a different
         * protocol.  The simple MUST send an Upgrade header field in a 426
         * response to indicate the required protocol(s) (Section 6.7 of
         * [RFC7230]).
         *
         *
         */
        @JvmStatic
        val HTTP_UPGRADE_REQUIRED = ResponseCode(426,"UPGRADE REQUIRED")


        /* 5XX: simple error */

        /**
         * HTTP Status-Code 500: Internal simple Error.
         */
        @Deprecated("it is misplaced and shouldn't have existed.")
        @JvmStatic
        val HTTP_SERVER_ERROR = ResponseCode(500,"SERVER ERROR")

        /**
         * HTTP Status-Code 500: Internal simple Error.
         */
        @JvmStatic
        val HTTP_INTERNAL_ERROR = ResponseCode(500,"INTERNAL ERROR")

        /**
         * HTTP Status-Code 501: Not Implemented.
         */
        @JvmStatic
        val HTTP_NOT_IMPLEMENTED = ResponseCode(501,"NOT IMPLEMENTED")

        /**
         * HTTP Status-Code 502: Bad Gateway.
         */
        @JvmStatic
        val HTTP_BAD_GATEWAY = ResponseCode(502,"BAD GATEWAY")

        /**
         * HTTP Status-Code 503: Service Unavailable.
         */
        @JvmStatic
        val HTTP_UNAVAILABLE = ResponseCode(503,"UNAVAILABLE")

        /**
         * HTTP Status-Code 504: Gateway Timeout.
         */
        @JvmStatic
        val HTTP_GATEWAY_TIMEOUT = ResponseCode(504,"GATEWAY TIMEOUT")

        /**
         * HTTP Status-Code 505: HTTP Version Not Supported.
         */
        @JvmStatic
        val HTTP_VERSION = ResponseCode(505,"VERSION")
    }


}