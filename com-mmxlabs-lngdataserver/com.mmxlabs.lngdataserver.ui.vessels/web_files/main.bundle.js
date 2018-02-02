webpackJsonp([1],{

/***/ "../../../../../generated/ports-locations/api.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_canals_service__ = __webpack_require__("../../../../../generated/ports-locations/api/canals.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__api_countries_service__ = __webpack_require__("../../../../../generated/ports-locations/api/countries.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__api_distances_service__ = __webpack_require__("../../../../../generated/ports-locations/api/distances.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__api_locations_service__ = __webpack_require__("../../../../../generated/ports-locations/api/locations.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ApiModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};








var ApiModule = ApiModule_1 = (function () {
    function ApiModule(parentModule) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import your base AppModule only.');
        }
    }
    ApiModule.forRoot = function (configurationFactory) {
        return {
            ngModule: ApiModule_1,
            providers: [{ provide: __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */], useFactory: configurationFactory }]
        };
    };
    return ApiModule;
}());
ApiModule = ApiModule_1 = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_common__["CommonModule"], __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["a" /* HttpClientModule */]],
        declarations: [],
        exports: [],
        providers: [
            __WEBPACK_IMPORTED_MODULE_4__api_canals_service__["a" /* CanalsService */],
            __WEBPACK_IMPORTED_MODULE_5__api_countries_service__["a" /* CountriesService */],
            __WEBPACK_IMPORTED_MODULE_6__api_distances_service__["a" /* DistancesService */],
            __WEBPACK_IMPORTED_MODULE_7__api_locations_service__["a" /* LocationsService */]
        ]
    }),
    __param(0, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(0, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["SkipSelf"])()),
    __metadata("design:paramtypes", [ApiModule])
], ApiModule);

var ApiModule_1;
//# sourceMappingURL=api.module.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/api/api.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__canals_service__ = __webpack_require__("../../../../../generated/ports-locations/api/canals.service.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__countries_service__ = __webpack_require__("../../../../../generated/ports-locations/api/countries.service.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__distances_service__ = __webpack_require__("../../../../../generated/ports-locations/api/distances.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "b", function() { return __WEBPACK_IMPORTED_MODULE_2__distances_service__["a"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__locations_service__ = __webpack_require__("../../../../../generated/ports-locations/api/locations.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "a", function() { return __WEBPACK_IMPORTED_MODULE_3__locations_service__["a"]; });
/* unused harmony export APIS */








var APIS = [__WEBPACK_IMPORTED_MODULE_0__canals_service__["a" /* CanalsService */], __WEBPACK_IMPORTED_MODULE_1__countries_service__["a" /* CountriesService */], __WEBPACK_IMPORTED_MODULE_2__distances_service__["a" /* DistancesService */], __WEBPACK_IMPORTED_MODULE_3__locations_service__["a" /* LocationsService */]];
//# sourceMappingURL=api.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/api/canals.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CanalsService; });
/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};




var CanalsService = (function () {
    function CanalsService(httpClient, basePath, configuration) {
        this.httpClient = httpClient;
        this.basePath = 'https://localhost:54977';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    CanalsService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    CanalsService.prototype.getCanalsUsingGET = function (observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/canals", {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    return CanalsService;
}());
CanalsService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_2__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], CanalsService);

var _a, _b;
//# sourceMappingURL=canals.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/api/countries.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CountriesService; });
/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};




var CountriesService = (function () {
    function CountriesService(httpClient, basePath, configuration) {
        this.httpClient = httpClient;
        this.basePath = 'https://localhost:54977';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    CountriesService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    CountriesService.prototype.getCountriesUsingGET = function (observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/countries", {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    return CountriesService;
}());
CountriesService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_2__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], CountriesService);

var _a, _b;
//# sourceMappingURL=countries.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/api/distances.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__encoder__ = __webpack_require__("../../../../../generated/ports-locations/encoder.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DistancesService; });
/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};





var DistancesService = (function () {
    function DistancesService(httpClient, basePath, configuration) {
        this.httpClient = httpClient;
        this.basePath = 'https://localhost:54977';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    DistancesService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    DistancesService.prototype.getDistanceMatrixUsingGET = function (open, v, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (open !== undefined) {
            queryParameters = queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            'application/json',
            'text/csv'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distances", {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.getDistanceUpdateUsingGET = function (observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            'application/json'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distances/version_notification", {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.getDistanceUsingGET = function (srcLocation, dstLocation, open, v, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (srcLocation === null || srcLocation === undefined) {
            throw new Error('Required parameter srcLocation was null or undefined when calling getDistanceUsingGET.');
        }
        if (dstLocation === null || dstLocation === undefined) {
            throw new Error('Required parameter dstLocation was null or undefined when calling getDistanceUsingGET.');
        }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (open !== undefined) {
            queryParameters = queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distance/" + encodeURIComponent(String(srcLocation)) + "/" + encodeURIComponent(String(dstLocation)), {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.getFullVersionUsingGET = function (versionId, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (versionId === null || versionId === undefined) {
            throw new Error('Required parameter versionId was null or undefined when calling getFullVersionUsingGET.');
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distances/sync/versions/" + encodeURIComponent(String(versionId)), {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.getRouteUsingGET = function (srcLocation, dstLocation, open, v, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (srcLocation === null || srcLocation === undefined) {
            throw new Error('Required parameter srcLocation was null or undefined when calling getRouteUsingGET.');
        }
        if (dstLocation === null || dstLocation === undefined) {
            throw new Error('Required parameter dstLocation was null or undefined when calling getRouteUsingGET.');
        }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (open !== undefined) {
            queryParameters = queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distance/route/" + encodeURIComponent(String(srcLocation)) + "/" + encodeURIComponent(String(dstLocation)), {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.getVersionsUsingGET = function (observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/distances/versions", {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.postFullVersionUsingPOST = function (version, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (version === null || version === undefined) {
            throw new Error('Required parameter version was null or undefined when calling postFullVersionUsingPOST.');
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        var httpContentTypeSelected = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }
        return this.httpClient.post(this.basePath + "/distances/sync/versions", version, {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.postSyncRequestUsingPOST = function (publishRequest, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (publishRequest === null || publishRequest === undefined) {
            throw new Error('Required parameter publishRequest was null or undefined when calling postSyncRequestUsingPOST.');
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        var httpContentTypeSelected = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }
        return this.httpClient.post(this.basePath + "/distances/sync/publish", publishRequest, {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    DistancesService.prototype.updateDistanceUsingPUT = function (srcLocation, dstLocation, distanceEditRequest, v, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (srcLocation === null || srcLocation === undefined) {
            throw new Error('Required parameter srcLocation was null or undefined when calling updateDistanceUsingPUT.');
        }
        if (dstLocation === null || dstLocation === undefined) {
            throw new Error('Required parameter dstLocation was null or undefined when calling updateDistanceUsingPUT.');
        }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        var httpContentTypeSelected = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }
        return this.httpClient.put(this.basePath + "/distance/" + encodeURIComponent(String(srcLocation)) + "/" + encodeURIComponent(String(dstLocation)), distanceEditRequest, {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    return DistancesService;
}());
DistancesService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], DistancesService);

var _a, _b;
//# sourceMappingURL=distances.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/api/locations.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__encoder__ = __webpack_require__("../../../../../generated/ports-locations/encoder.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LocationsService; });
/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};





var LocationsService = (function () {
    function LocationsService(httpClient, basePath, configuration) {
        this.httpClient = httpClient;
        this.basePath = 'https://localhost:54977';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["b" /* HttpHeaders */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    LocationsService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    LocationsService.prototype.getLocationUsingGET = function (location, v, fuzzy, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (location === null || location === undefined) {
            throw new Error('Required parameter location was null or undefined when calling getLocationUsingGET.');
        }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        if (fuzzy !== undefined) {
            queryParameters = queryParameters.set('fuzzy', fuzzy);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/locations/" + encodeURIComponent(String(location)), {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    LocationsService.prototype.getLocationsUsingGET = function (v, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]({ encoder: new __WEBPACK_IMPORTED_MODULE_2__encoder__["a" /* CustomHttpUrlEncodingCodec */]() });
        if (v !== undefined) {
            queryParameters = queryParameters.set('v', v);
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.get(this.basePath + "/locations", {
            params: queryParameters,
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    LocationsService.prototype.patchLocationUsingPATCH = function (mmxId, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (mmxId === null || mmxId === undefined) {
            throw new Error('Required parameter mmxId was null or undefined when calling patchLocationUsingPATCH.');
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        return this.httpClient.patch(this.basePath + "/locations/" + encodeURIComponent(String(mmxId)), null, {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    LocationsService.prototype.postLocationUsingPOST = function (location, observe, reportProgress) {
        if (observe === void 0) { observe = 'body'; }
        if (reportProgress === void 0) { reportProgress = false; }
        if (location === null || location === undefined) {
            throw new Error('Required parameter location was null or undefined when calling postLocationUsingPOST.');
        }
        var headers = this.defaultHeaders;
        // to determine the Accept header
        var httpHeaderAccepts = [
            '*/*'
        ];
        var httpHeaderAcceptSelected = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected != undefined) {
            headers = headers.set("Accept", httpHeaderAcceptSelected);
        }
        // to determine the Content-Type header
        var consumes = [
            'application/json'
        ];
        var httpContentTypeSelected = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected != undefined) {
            headers = headers.set("Content-Type", httpContentTypeSelected);
        }
        return this.httpClient.post(this.basePath + "/locations", location, {
            withCredentials: this.configuration.withCredentials,
            headers: headers,
            observe: observe,
            reportProgress: reportProgress
        });
    };
    return LocationsService;
}());
LocationsService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], LocationsService);

var _a, _b;
//# sourceMappingURL=locations.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/configuration.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Configuration; });
var Configuration = (function () {
    function Configuration(configurationParameters) {
        if (configurationParameters === void 0) { configurationParameters = {}; }
        this.apiKeys = configurationParameters.apiKeys;
        this.username = configurationParameters.username;
        this.password = configurationParameters.password;
        this.accessToken = configurationParameters.accessToken;
        this.basePath = configurationParameters.basePath;
        this.withCredentials = configurationParameters.withCredentials;
    }
    /**
     * Select the correct content-type to use for a request.
     * Uses {@link Configuration#isJsonMime} to determine the correct content-type.
     * If no content type is found return the first found type if the contentTypes is not empty
     * @param {string[]} contentTypes - the array of content types that are available for selection
     * @returns {string} the selected content-type or <code>undefined</code> if no selection could be made.
     */
    Configuration.prototype.selectHeaderContentType = function (contentTypes) {
        var _this = this;
        if (contentTypes.length == 0) {
            return undefined;
        }
        var type = contentTypes.find(function (x) { return _this.isJsonMime(x); });
        if (type === undefined) {
            return contentTypes[0];
        }
        return type;
    };
    /**
     * Select the correct accept content-type to use for a request.
     * Uses {@link Configuration#isJsonMime} to determine the correct accept content-type.
     * If no content type is found return the first found type if the contentTypes is not empty
     * @param {string[]} accepts - the array of content types that are available for selection.
     * @returns {string} the selected content-type or <code>undefined</code> if no selection could be made.
     */
    Configuration.prototype.selectHeaderAccept = function (accepts) {
        var _this = this;
        if (accepts.length == 0) {
            return undefined;
        }
        var type = accepts.find(function (x) { return _this.isJsonMime(x); });
        if (type === undefined) {
            return accepts[0];
        }
        return type;
    };
    /**
     * Check if the given MIME is a JSON MIME.
     * JSON MIME examples:
     *   application/json
     *   application/json; charset=UTF8
     *   APPLICATION/JSON
     *   application/vnd.company+json
     * @param {string} mime - MIME (Multipurpose Internet Mail Extensions)
     * @return {boolean} True if the given MIME is JSON, false otherwise.
     */
    Configuration.prototype.isJsonMime = function (mime) {
        var jsonMime = new RegExp('^(application\/json|[^;/ \t]+\/[^;/ \t]+[+]json)[ \t]*(;.*)?$', 'i');
        return mime != null && (jsonMime.test(mime) || mime.toLowerCase() === 'application/json-patch+json');
    };
    return Configuration;
}());

//# sourceMappingURL=configuration.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/encoder.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CustomHttpUrlEncodingCodec; });
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();

/**
* CustomHttpUrlEncodingCodec
* Fix plus sign (+) not encoding, so sent as blank space
* See: https://github.com/angular/angular/issues/11058#issuecomment-247367318
*/
var CustomHttpUrlEncodingCodec = (function (_super) {
    __extends(CustomHttpUrlEncodingCodec, _super);
    function CustomHttpUrlEncodingCodec() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    CustomHttpUrlEncodingCodec.prototype.encodeKey = function (k) {
        k = _super.prototype.encodeKey.call(this, k);
        return k.replace(/\+/gi, '%2B');
    };
    CustomHttpUrlEncodingCodec.prototype.encodeValue = function (v) {
        v = _super.prototype.encodeValue.call(this, v);
        return v.replace(/\+/gi, '%2B');
    };
    return CustomHttpUrlEncodingCodec;
}(__WEBPACK_IMPORTED_MODULE_0__angular_common_http__["e" /* HttpUrlEncodingCodec */]));

//# sourceMappingURL=encoder.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/index.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__api_api__ = __webpack_require__("../../../../../generated/ports-locations/api/api.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "LocationsService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["a"]; });
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "DistancesService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["b"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__model_models__ = __webpack_require__("../../../../../generated/ports-locations/model/models.ts");
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__model_models__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_1__model_models__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-locations/configuration.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_module__ = __webpack_require__("../../../../../generated/ports-locations/api.module.ts");
/* unused harmony namespace reexport */





//# sourceMappingURL=index.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/country.ts":
/***/ (function(module, exports) {

//# sourceMappingURL=country.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/distanceEditRequest.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=distanceEditRequest.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/futureVersion.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=futureVersion.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/geographicPoint.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=geographicPoint.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/identifier.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=identifier.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/location.ts":
/***/ (function(module, exports) {

//# sourceMappingURL=location.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/models.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__country__ = __webpack_require__("../../../../../generated/ports-locations/model/country.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__country___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__country__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_0__country__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_0__country__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__ = __webpack_require__("../../../../../generated/ports-locations/model/distanceEditRequest.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__futureVersion__ = __webpack_require__("../../../../../generated/ports-locations/model/futureVersion.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__futureVersion___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__futureVersion__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_2__futureVersion__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_2__futureVersion__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__geographicPoint__ = __webpack_require__("../../../../../generated/ports-locations/model/geographicPoint.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__geographicPoint___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__geographicPoint__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_3__geographicPoint__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_3__geographicPoint__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__identifier__ = __webpack_require__("../../../../../generated/ports-locations/model/identifier.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__identifier___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__identifier__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_4__identifier__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_4__identifier__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__location__ = __webpack_require__("../../../../../generated/ports-locations/model/location.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__location___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__location__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_5__location__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_5__location__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__objectId__ = __webpack_require__("../../../../../generated/ports-locations/model/objectId.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__objectId___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__objectId__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_6__objectId__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_6__objectId__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__publishRequest__ = __webpack_require__("../../../../../generated/ports-locations/model/publishRequest.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__publishRequest___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7__publishRequest__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_7__publishRequest__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_7__publishRequest__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__responseEntity__ = __webpack_require__("../../../../../generated/ports-locations/model/responseEntity.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__route__ = __webpack_require__("../../../../../generated/ports-locations/model/route.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__route___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_9__route__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_9__route__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_9__route__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__routes__ = __webpack_require__("../../../../../generated/ports-locations/model/routes.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__routes___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_10__routes__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_10__routes__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_10__routes__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__routingPoint__ = __webpack_require__("../../../../../generated/ports-locations/model/routingPoint.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__routingPoint___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_11__routingPoint__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_11__routingPoint__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_11__routingPoint__["Location"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__version__ = __webpack_require__("../../../../../generated/ports-locations/model/version.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__version___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_12__version__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_12__version__, "Location")) __webpack_require__.d(__webpack_exports__, "Location", function() { return __WEBPACK_IMPORTED_MODULE_12__version__["Location"]; });













//# sourceMappingURL=models.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/objectId.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=objectId.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/publishRequest.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=publishRequest.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/responseEntity.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export ResponseEntity */
/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
var ResponseEntity;
(function (ResponseEntity) {
    ResponseEntity.StatusCodeEnum = {
        _100: '100',
        _101: '101',
        _102: '102',
        _103: '103',
        _200: '200',
        _201: '201',
        _202: '202',
        _203: '203',
        _204: '204',
        _205: '205',
        _206: '206',
        _207: '207',
        _208: '208',
        _226: '226',
        _300: '300',
        _301: '301',
        _302: '302',
        _303: '303',
        _304: '304',
        _305: '305',
        _307: '307',
        _308: '308',
        _400: '400',
        _401: '401',
        _402: '402',
        _403: '403',
        _404: '404',
        _405: '405',
        _406: '406',
        _407: '407',
        _408: '408',
        _409: '409',
        _410: '410',
        _411: '411',
        _412: '412',
        _413: '413',
        _414: '414',
        _415: '415',
        _416: '416',
        _417: '417',
        _418: '418',
        _419: '419',
        _420: '420',
        _421: '421',
        _422: '422',
        _423: '423',
        _424: '424',
        _426: '426',
        _428: '428',
        _429: '429',
        _431: '431',
        _451: '451',
        _500: '500',
        _501: '501',
        _502: '502',
        _503: '503',
        _504: '504',
        _505: '505',
        _506: '506',
        _507: '507',
        _508: '508',
        _509: '509',
        _510: '510',
        _511: '511'
    };
})(ResponseEntity || (ResponseEntity = {}));
//# sourceMappingURL=responseEntity.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/route.ts":
/***/ (function(module, exports) {

/**
 * Minimax Labs Ports and Distances Service
 * ## Canals ### Panama Pacific entrance port: Balboa  <br>  Caribbean entrance port: Colon  <br> Balboa <> Colon: 39.2 nmi ### Suez Mediterranean entrance port: Port Said   <br>Red Sea entrance port: Suez   <br>Port Said <> Suez: 102.3 nmi ## Versions The distance data is versioned. Query the distance endpoint to get a list of all available versions. If no version is specified the latest available version is used. ## Routing Points Users can open Routing Points to get results for voyages __potentially__ passing open Routing Points. The service will always return the shortest voyage, only passing open routing points if it is the shortest option. Forcing a routing point has to be done manually by sending multiple requests, e.g. to get the distance from Zeebrugge to Barcelona via Panama:   Zeebrugge > Colon: 4757.215   Panama Canal length: 39.2   Colon > Barcelona:  11006.962   Total: 15803.3  ### Available Routing Points | Routing Point | Description  | | ------------- | -----------  | | SUZ           | Suez Canal   | | PAN           | Panama Canal | ## Errors Codes When querying a single distance, errors will be returned as a 4xx or 5xx HTTP status code with a description.When querying a whole matrix the distance related errors will be returned in the distance field.  | Code        | Description           | | ----------- | ------------- | | e: no pc    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no rp    | We don't currently have distance data for this route. Please contact us directly and we will seek to resolve the issue. | | e: no us    | Our upstream distance provider does not have a distance for this route. Please contact us directly and we will seek to resolve the issue. |
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=route.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/routes.ts":
/***/ (function(module, exports) {

//# sourceMappingURL=routes.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/routingPoint.ts":
/***/ (function(module, exports) {

//# sourceMappingURL=routingPoint.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/model/version.ts":
/***/ (function(module, exports) {

//# sourceMappingURL=version.js.map

/***/ }),

/***/ "../../../../../generated/ports-locations/variables.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BASE_PATH; });
/* unused harmony export COLLECTION_FORMATS */

var BASE_PATH = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["InjectionToken"]('basePath');
var COLLECTION_FORMATS = {
    'csv': ',',
    'tsv': '   ',
    'ssv': ' ',
    'pipes': '|'
};
//# sourceMappingURL=variables.js.map

/***/ }),

/***/ "../../../../../generated/ports/api.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports/configuration.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_port_service__ = __webpack_require__("../../../../../generated/ports/api/port.service.ts");
/* unused harmony export ApiModule */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var ApiModule = ApiModule_1 = (function () {
    function ApiModule() {
    }
    ApiModule.forConfig = function (configurationFactory) {
        return {
            ngModule: ApiModule_1,
            providers: [{ provide: __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */], useFactory: configurationFactory }]
        };
    };
    return ApiModule;
}());
ApiModule = ApiModule_1 = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_common__["CommonModule"], __WEBPACK_IMPORTED_MODULE_2__angular_http__["a" /* HttpModule */]],
        declarations: [],
        exports: [],
        providers: [__WEBPACK_IMPORTED_MODULE_4__api_port_service__["a" /* PortService */]]
    })
], ApiModule);

var ApiModule_1;
//# sourceMappingURL=api.module.js.map

/***/ }),

/***/ "../../../../../generated/ports/api/api.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__port_service__ = __webpack_require__("../../../../../generated/ports/api/port.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "a", function() { return __WEBPACK_IMPORTED_MODULE_0__port_service__["a"]; });
/* unused harmony export APIS */


var APIS = [__WEBPACK_IMPORTED_MODULE_0__port_service__["a" /* PortService */]];
//# sourceMappingURL=api.js.map

/***/ }),

/***/ "../../../../../generated/ports/api/port.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__rxjs_operators__ = __webpack_require__("../../../../../generated/ports/rxjs-operators.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PortService; });
/**
 * Minimax Port Service
 * This service holds LNG specific port data. It is an enrichment to the location information available in the [distances service](null/distances).
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
/* tslint:disable:no-unused-variable member-ordering */






var PortService = (function () {
    function PortService(http, basePath, configuration) {
        this.http = http;
        this.basePath = 'http://localhost:8095';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     *
     * Extends object by coping non-existing properties.
     * @param objA object to be extended
     * @param objB source object
     */
    PortService.prototype.extendObj = function (objA, objB) {
        for (var key in objB) {
            if (objB.hasOwnProperty(key)) {
                objA[key] = objB[key];
            }
        }
        return objA;
    };
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    PortService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    /**
     * POST a list of ports. This only triggers one new version with all the ports added to it.
     * The &#x60;defaultStartTime&#x60; field of the Port is a JSR310 time string (ISO 8601), e.g. HH:mm:ss -&gt; 07:30:00
     * @param ports ports
     */
    PortService.prototype.bulkInsertUsingPOST = function (ports, extraHttpRequestParams) {
        return this.bulkInsertUsingPOSTWithHttpInfo(ports, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET all ports
     *
     */
    PortService.prototype.fetchAllUsingGET = function (extraHttpRequestParams) {
        return this.fetchAllUsingGETWithHttpInfo(extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET a port
     *
     * @param mmxId mmxId
     */
    PortService.prototype.fetchUsingGET = function (mmxId, extraHttpRequestParams) {
        return this.fetchUsingGETWithHttpInfo(mmxId, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET versions
     *
     */
    PortService.prototype.fetchVersionsUsingGET = function (extraHttpRequestParams) {
        return this.fetchVersionsUsingGETWithHttpInfo(extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * POST a port
     * The &#x60;defaultStartTime&#x60; field of the Port is a JSR310 time string (ISO 8601), e.g. HH:mm:ss -&gt; 07:30:00   versions and mmxId field **will be ignored**.
     * @param port port
     */
    PortService.prototype.insertUsingPOST = function (port, extraHttpRequestParams) {
        return this.insertUsingPOSTWithHttpInfo(port, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * PATCH a port attribute change
     * Change a port attribute.
     * @param mmxId mmxId
     */
    PortService.prototype.patchUsingPATCH = function (mmxId, extraHttpRequestParams) {
        return this.patchUsingPATCHWithHttpInfo(mmxId, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * POST a list of ports. This only triggers one new version with all the ports added to it.
     * The &#x60;defaultStartTime&#x60; field of the Port is a JSR310 time string (ISO 8601), e.g. HH:mm:ss -&gt; 07:30:00
     * @param ports ports
     */
    PortService.prototype.bulkInsertUsingPOSTWithHttpInfo = function (ports, extraHttpRequestParams) {
        var path = this.basePath + '/bulk/ports';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'ports' is not null or undefined
        if (ports === null || ports === undefined) {
            throw new Error('Required parameter ports was null or undefined when calling bulkInsertUsingPOST.');
        }
        // to determine the Accept header
        var produces = [
            '*/*'
        ];
        headers.set('Content-Type', 'application/json');
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Post,
            headers: headers,
            body: ports == null ? '' : JSON.stringify(ports),
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET all ports
     *
     */
    PortService.prototype.fetchAllUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/ports';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET a port
     *
     * @param mmxId mmxId
     */
    PortService.prototype.fetchUsingGETWithHttpInfo = function (mmxId, extraHttpRequestParams) {
        var path = this.basePath + '/ports/${mmxId}'
            .replace('${' + 'mmxId' + '}', String(mmxId));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'mmxId' is not null or undefined
        if (mmxId === null || mmxId === undefined) {
            throw new Error('Required parameter mmxId was null or undefined when calling fetchUsingGET.');
        }
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET versions
     *
     */
    PortService.prototype.fetchVersionsUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/ports/versions';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * POST a port
     * The &#x60;defaultStartTime&#x60; field of the Port is a JSR310 time string (ISO 8601), e.g. HH:mm:ss -&gt; 07:30:00   versions and mmxId field **will be ignored**.
     * @param port port
     */
    PortService.prototype.insertUsingPOSTWithHttpInfo = function (port, extraHttpRequestParams) {
        var path = this.basePath + '/ports';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'port' is not null or undefined
        if (port === null || port === undefined) {
            throw new Error('Required parameter port was null or undefined when calling insertUsingPOST.');
        }
        // to determine the Accept header
        var produces = [
            '*/*'
        ];
        headers.set('Content-Type', 'application/json');
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Post,
            headers: headers,
            body: port == null ? '' : JSON.stringify(port),
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * PATCH a port attribute change
     * Change a port attribute.
     * @param mmxId mmxId
     */
    PortService.prototype.patchUsingPATCHWithHttpInfo = function (mmxId, extraHttpRequestParams) {
        var path = this.basePath + '/ports/${mmxId}'
            .replace('${' + 'mmxId' + '}', String(mmxId));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'mmxId' is not null or undefined
        if (mmxId === null || mmxId === undefined) {
            throw new Error('Required parameter mmxId was null or undefined when calling patchUsingPATCH.');
        }
        // to determine the Accept header
        var produces = [
            '*/*'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Patch,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    return PortService;
}());
PortService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], PortService);

var _a, _b;
//# sourceMappingURL=port.service.js.map

/***/ }),

/***/ "../../../../../generated/ports/configuration.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Configuration; });
var Configuration = (function () {
    function Configuration(configurationParameters) {
        if (configurationParameters === void 0) { configurationParameters = {}; }
        this.apiKeys = configurationParameters.apiKeys;
        this.username = configurationParameters.username;
        this.password = configurationParameters.password;
        this.accessToken = configurationParameters.accessToken;
        this.basePath = configurationParameters.basePath;
        this.withCredentials = configurationParameters.withCredentials;
    }
    return Configuration;
}());

//# sourceMappingURL=configuration.js.map

/***/ }),

/***/ "../../../../../generated/ports/index.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__api_api__ = __webpack_require__("../../../../../generated/ports/api/api.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "PortService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["a"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__model_models__ = __webpack_require__("../../../../../generated/ports/model/models.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/ports/variables.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports/configuration.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_module__ = __webpack_require__("../../../../../generated/ports/api.module.ts");
/* unused harmony namespace reexport */





//# sourceMappingURL=index.js.map

/***/ }),

/***/ "../../../../../generated/ports/model/models.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__port__ = __webpack_require__("../../../../../generated/ports/model/port.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__version__ = __webpack_require__("../../../../../generated/ports/model/version.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__version___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__version__);
/* unused harmony namespace reexport */


//# sourceMappingURL=models.js.map

/***/ }),

/***/ "../../../../../generated/ports/model/port.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* unused harmony export Port */
/**
 * Minimax Port Service
 * This service holds LNG specific port data. It is an enrichment to the location information available in the [distances service](null/distances).
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
var Port;
(function (Port) {
    var CapabilitiesEnum;
    (function (CapabilitiesEnum) {
        CapabilitiesEnum[CapabilitiesEnum["LOAD"] = 'LOAD'] = "LOAD";
        CapabilitiesEnum[CapabilitiesEnum["DISCHARGE"] = 'DISCHARGE'] = "DISCHARGE";
        CapabilitiesEnum[CapabilitiesEnum["DRYDOCK"] = 'DRYDOCK'] = "DRYDOCK";
        CapabilitiesEnum[CapabilitiesEnum["MAINTENANCE"] = 'MAINTENANCE'] = "MAINTENANCE";
    })(CapabilitiesEnum = Port.CapabilitiesEnum || (Port.CapabilitiesEnum = {}));
})(Port || (Port = {}));
//# sourceMappingURL=port.js.map

/***/ }),

/***/ "../../../../../generated/ports/model/version.ts":
/***/ (function(module, exports) {

/**
 * Minimax Port Service
 * This service holds LNG specific port data. It is an enrichment to the location information available in the [distances service](null/distances).
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=version.js.map

/***/ }),

/***/ "../../../../../generated/ports/rxjs-operators.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw__ = __webpack_require__("../../../../rxjs/add/observable/throw.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch__ = __webpack_require__("../../../../rxjs/add/operator/catch.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__ = __webpack_require__("../../../../rxjs/add/operator/map.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__);
// RxJS imports according to https://angular.io/docs/ts/latest/guide/server-communication.html#!#rxjs
// See node_module/rxjs/Rxjs.js
// Import just the rxjs statics and operators we need for THIS app.
// Statics

// Operators


//# sourceMappingURL=rxjs-operators.js.map

/***/ }),

/***/ "../../../../../generated/ports/variables.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BASE_PATH; });
/* unused harmony export COLLECTION_FORMATS */

var BASE_PATH = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["InjectionToken"]('basePath');
var COLLECTION_FORMATS = {
    'csv': ',',
    'tsv': '   ',
    'ssv': ' ',
    'pipes': '|'
};
//# sourceMappingURL=variables.js.map

/***/ }),

/***/ "../../../../../generated/vessels/api.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/vessels/configuration.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_vessels_service__ = __webpack_require__("../../../../../generated/vessels/api/vessels.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ApiModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var ApiModule = ApiModule_1 = (function () {
    function ApiModule() {
    }
    ApiModule.forConfig = function (configurationFactory) {
        return {
            ngModule: ApiModule_1,
            providers: [{ provide: __WEBPACK_IMPORTED_MODULE_3__configuration__["a" /* Configuration */], useFactory: configurationFactory }]
        };
    };
    return ApiModule;
}());
ApiModule = ApiModule_1 = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_common__["CommonModule"], __WEBPACK_IMPORTED_MODULE_2__angular_http__["a" /* HttpModule */]],
        declarations: [],
        exports: [],
        providers: [__WEBPACK_IMPORTED_MODULE_4__api_vessels_service__["a" /* VesselsService */]]
    })
], ApiModule);

var ApiModule_1;
//# sourceMappingURL=api.module.js.map

/***/ }),

/***/ "../../../../../generated/vessels/api/api.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__vessels_service__ = __webpack_require__("../../../../../generated/vessels/api/vessels.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "a", function() { return __WEBPACK_IMPORTED_MODULE_0__vessels_service__["a"]; });
/* unused harmony export APIS */


var APIS = [__WEBPACK_IMPORTED_MODULE_0__vessels_service__["a" /* VesselsService */]];
//# sourceMappingURL=api.js.map

/***/ }),

/***/ "../../../../../generated/vessels/api/vessels.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__rxjs_operators__ = __webpack_require__("../../../../../generated/vessels/rxjs-operators.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/vessels/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/vessels/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return VesselsService; });
/**
 * Minimax Vessel Service
 * Complete vessel data for costs and trading P&L calculations. Business user data, with sample data for a range of standard vessel types.
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
/* tslint:disable:no-unused-variable member-ordering */






var VesselsService = (function () {
    function VesselsService(http, basePath, configuration) {
        this.http = http;
        this.basePath = 'https://localhost:8090';
        this.defaultHeaders = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */]();
        this.configuration = new __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]();
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }
    }
    /**
     *
     * Extends object by coping non-existing properties.
     * @param objA object to be extended
     * @param objB source object
     */
    VesselsService.prototype.extendObj = function (objA, objB) {
        for (var key in objB) {
            if (objB.hasOwnProperty(key)) {
                objA[key] = objB[key];
            }
        }
        return objA;
    };
    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    VesselsService.prototype.canConsumeForm = function (consumes) {
        var form = 'multipart/form-data';
        for (var _i = 0, consumes_1 = consumes; _i < consumes_1.length; _i++) {
            var consume = consumes_1[_i];
            if (form === consume) {
                return true;
            }
        }
        return false;
    };
    /**
     * GET all version
     * Get all versions
     */
    VesselsService.prototype.getVersionsUsingGET = function (extraHttpRequestParams) {
        return this.getVersionsUsingGETWithHttpInfo(extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET a vessel
     * Get a specific vessel
     * @param vesselId vesselId
     */
    VesselsService.prototype.getVesselUsingGET = function (vesselId, extraHttpRequestParams) {
        return this.getVesselUsingGETWithHttpInfo(vesselId, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET all vessel
     * Get all vessels
     */
    VesselsService.prototype.getVesselsUsingGET = function (extraHttpRequestParams) {
        return this.getVesselsUsingGETWithHttpInfo(extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * POST a new vessel
     * Post a new vessel to the database. Leave out the mmxxId field. If it is provided it will be overwritten when persisting!
     * @param vessel vessel
     */
    VesselsService.prototype.insertVesselUsingPOST = function (vessel, extraHttpRequestParams) {
        return this.insertVesselUsingPOSTWithHttpInfo(vessel, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * POST bulk post vessel
     * Post a new vessel to the database. Leave out the mmxxId field. If it is provided it will be overwritten when persisting!
     * @param vessels vessels
     */
    VesselsService.prototype.insertVesselsUsingPOST = function (vessels, extraHttpRequestParams) {
        return this.insertVesselsUsingPOSTWithHttpInfo(vessels, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET remove a vessel
     * Remove a specific vessel
     * @param vesselId vesselId
     */
    VesselsService.prototype.removeVesselUsingGET = function (vesselId, extraHttpRequestParams) {
        return this.removeVesselUsingGETWithHttpInfo(vesselId, extraHttpRequestParams)
            .map(function (response) {
            if (response.status === 204) {
                return undefined;
            }
            else {
                return response.json() || {};
            }
        });
    };
    /**
     * GET all version
     * Get all versions
     */
    VesselsService.prototype.getVersionsUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/versions';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET a vessel
     * Get a specific vessel
     * @param vesselId vesselId
     */
    VesselsService.prototype.getVesselUsingGETWithHttpInfo = function (vesselId, extraHttpRequestParams) {
        var path = this.basePath + '/vessels/${vesselId}'
            .replace('${' + 'vesselId' + '}', String(vesselId));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'vesselId' is not null or undefined
        if (vesselId === null || vesselId === undefined) {
            throw new Error('Required parameter vesselId was null or undefined when calling getVesselUsingGET.');
        }
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET all vessel
     * Get all vessels
     */
    VesselsService.prototype.getVesselsUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/vessels';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * POST a new vessel
     * Post a new vessel to the database. Leave out the mmxxId field. If it is provided it will be overwritten when persisting!
     * @param vessel vessel
     */
    VesselsService.prototype.insertVesselUsingPOSTWithHttpInfo = function (vessel, extraHttpRequestParams) {
        var path = this.basePath + '/vessel';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'vessel' is not null or undefined
        if (vessel === null || vessel === undefined) {
            throw new Error('Required parameter vessel was null or undefined when calling insertVesselUsingPOST.');
        }
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        headers.set('Content-Type', 'application/json');
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Post,
            headers: headers,
            body: vessel == null ? '' : JSON.stringify(vessel),
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * POST bulk post vessel
     * Post a new vessel to the database. Leave out the mmxxId field. If it is provided it will be overwritten when persisting!
     * @param vessels vessels
     */
    VesselsService.prototype.insertVesselsUsingPOSTWithHttpInfo = function (vessels, extraHttpRequestParams) {
        var path = this.basePath + '/vessels';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'vessels' is not null or undefined
        if (vessels === null || vessels === undefined) {
            throw new Error('Required parameter vessels was null or undefined when calling insertVesselsUsingPOST.');
        }
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        headers.set('Content-Type', 'application/json');
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Post,
            headers: headers,
            body: vessels == null ? '' : JSON.stringify(vessels),
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    /**
     * GET remove a vessel
     * Remove a specific vessel
     * @param vesselId vesselId
     */
    VesselsService.prototype.removeVesselUsingGETWithHttpInfo = function (vesselId, extraHttpRequestParams) {
        var path = this.basePath + '/vessels/remove/${vesselId}'
            .replace('${' + 'vesselId' + '}', String(vesselId));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'vesselId' is not null or undefined
        if (vesselId === null || vesselId === undefined) {
            throw new Error('Required parameter vesselId was null or undefined when calling removeVesselUsingGET.');
        }
        // to determine the Accept header
        var produces = [
            'application/json'
        ];
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Get,
            headers: headers,
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    return VesselsService;
}());
VesselsService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], VesselsService);

var _a, _b;
//# sourceMappingURL=vessels.service.js.map

/***/ }),

/***/ "../../../../../generated/vessels/configuration.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return Configuration; });
var Configuration = (function () {
    function Configuration(configurationParameters) {
        if (configurationParameters === void 0) { configurationParameters = {}; }
        this.apiKeys = configurationParameters.apiKeys;
        this.username = configurationParameters.username;
        this.password = configurationParameters.password;
        this.accessToken = configurationParameters.accessToken;
        this.basePath = configurationParameters.basePath;
        this.withCredentials = configurationParameters.withCredentials;
    }
    return Configuration;
}());

//# sourceMappingURL=configuration.js.map

/***/ }),

/***/ "../../../../../generated/vessels/index.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__api_api__ = __webpack_require__("../../../../../generated/vessels/api/api.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "VesselsService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["a"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__model_models__ = __webpack_require__("../../../../../generated/vessels/model/models.ts");
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__model_models__, "VesselStateAttributes")) __webpack_require__.d(__webpack_exports__, "VesselStateAttributes", function() { return __WEBPACK_IMPORTED_MODULE_1__model_models__["VesselStateAttributes"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/vessels/variables.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/vessels/configuration.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_module__ = __webpack_require__("../../../../../generated/vessels/api.module.ts");
/* unused harmony namespace reexport */





//# sourceMappingURL=index.js.map

/***/ }),

/***/ "../../../../../generated/vessels/model/fuelConsumption.ts":
/***/ (function(module, exports) {

/**
 * Minimax Vessel Service
 * Complete vessel data for costs and trading P&L calculations. Business user data, with sample data for a range of standard vessel types.
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=fuelConsumption.js.map

/***/ }),

/***/ "../../../../../generated/vessels/model/models.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__fuelConsumption__ = __webpack_require__("../../../../../generated/vessels/model/fuelConsumption.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__fuelConsumption___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__fuelConsumption__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_0__fuelConsumption__, "VesselStateAttributes")) __webpack_require__.d(__webpack_exports__, "VesselStateAttributes", function() { return __WEBPACK_IMPORTED_MODULE_0__fuelConsumption__["VesselStateAttributes"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__version__ = __webpack_require__("../../../../../generated/vessels/model/version.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__version___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__version__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__version__, "VesselStateAttributes")) __webpack_require__.d(__webpack_exports__, "VesselStateAttributes", function() { return __WEBPACK_IMPORTED_MODULE_1__version__["VesselStateAttributes"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__vessel__ = __webpack_require__("../../../../../generated/vessels/model/vessel.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__vessel___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__vessel__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_2__vessel__, "VesselStateAttributes")) __webpack_require__.d(__webpack_exports__, "VesselStateAttributes", function() { return __WEBPACK_IMPORTED_MODULE_2__vessel__["VesselStateAttributes"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__vesselStateAttributes__ = __webpack_require__("../../../../../generated/vessels/model/vesselStateAttributes.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__vesselStateAttributes___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__vesselStateAttributes__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_3__vesselStateAttributes__, "VesselStateAttributes")) __webpack_require__.d(__webpack_exports__, "VesselStateAttributes", function() { return __WEBPACK_IMPORTED_MODULE_3__vesselStateAttributes__["VesselStateAttributes"]; });




//# sourceMappingURL=models.js.map

/***/ }),

/***/ "../../../../../generated/vessels/model/version.ts":
/***/ (function(module, exports) {

/**
 * Minimax Vessel Service
 * Complete vessel data for costs and trading P&L calculations. Business user data, with sample data for a range of standard vessel types.
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=version.js.map

/***/ }),

/***/ "../../../../../generated/vessels/model/vessel.ts":
/***/ (function(module, exports) {

/**
 * Minimax Vessel Service
 * Complete vessel data for costs and trading P&L calculations. Business user data, with sample data for a range of standard vessel types.
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=vessel.js.map

/***/ }),

/***/ "../../../../../generated/vessels/model/vesselStateAttributes.ts":
/***/ (function(module, exports) {

/**
 * Minimax Vessel Service
 * Complete vessel data for costs and trading P&L calculations. Business user data, with sample data for a range of standard vessel types.
 *
 * OpenAPI spec version: 0.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
//# sourceMappingURL=vesselStateAttributes.js.map

/***/ }),

/***/ "../../../../../generated/vessels/rxjs-operators.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw__ = __webpack_require__("../../../../rxjs/add/observable/throw.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_rxjs_add_observable_throw__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch__ = __webpack_require__("../../../../rxjs/add/operator/catch.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_rxjs_add_operator_catch__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__ = __webpack_require__("../../../../rxjs/add/operator/map.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_map__);
// RxJS imports according to https://angular.io/docs/ts/latest/guide/server-communication.html#!#rxjs
// See node_module/rxjs/Rxjs.js
// Import just the rxjs statics and operators we need for THIS app.
// Statics

// Operators


//# sourceMappingURL=rxjs-operators.js.map

/***/ }),

/***/ "../../../../../generated/vessels/variables.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BASE_PATH; });
/* unused harmony export COLLECTION_FORMATS */

var BASE_PATH = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["InjectionToken"]('basePath');
var COLLECTION_FORMATS = {
    'csv': ',',
    'tsv': '   ',
    'ssv': ' ',
    'pipes': '|'
};
//# sourceMappingURL=variables.js.map

/***/ }),

/***/ "../../../../../src async recursive":
/***/ (function(module, exports) {

function webpackEmptyContext(req) {
	throw new Error("Cannot find module '" + req + "'.");
}
webpackEmptyContext.keys = function() { return []; };
webpackEmptyContext.resolve = webpackEmptyContext;
module.exports = webpackEmptyContext;
webpackEmptyContext.id = "../../../../../src async recursive";

/***/ }),

/***/ "../../../../../src/app/app-routing.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__distance_matrix_distance_matrix_component__ = __webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__ports_ports_component__ = __webpack_require__("../../../../../src/app/ports/ports.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__locations_locations_component__ = __webpack_require__("../../../../../src/app/locations/locations.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__vessel_vessel_component__ = __webpack_require__("../../../../../src/app/vessel/vessel.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__vessels_vessels_component__ = __webpack_require__("../../../../../src/app/vessels/vessels.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__location_location_detail_component__ = __webpack_require__("../../../../../src/app/location/location-detail.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__pricing_pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__pricing_commodities_commodities_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__pricing_currencies_currencies_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__pricing_basefuel_basefuel_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/basefuel.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__pricing_charter_charter_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__pricing_commodities_commodities_editor_commodities_editor_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14__pricing_commodities_commodities_chart_commodities_chart_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__pricing_currencies_currencies_chart_currencies_chart_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16__pricing_currencies_currencies_editor_currencies_editor_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_17__pricing_basefuel_base_fuel_chart_base_fuel_chart_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_18__pricing_basefuel_base_fuel_editor_base_fuel_editor_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_19__pricing_charter_charter_chart_charter_chart_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_20__pricing_charter_charter_editor_charter_editor_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppRoutingModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





















var routes = [
    { path: '', redirectTo: '/distances', pathMatch: 'full' },
    { path: 'vessel/:version/:id', component: __WEBPACK_IMPORTED_MODULE_5__vessel_vessel_component__["a" /* VesselComponent */] },
    { path: 'vessels/:version', component: __WEBPACK_IMPORTED_MODULE_6__vessels_vessels_component__["a" /* VesselsComponent */] },
    { path: 'distances', component: __WEBPACK_IMPORTED_MODULE_2__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */] },
    { path: 'distances/:version', component: __WEBPACK_IMPORTED_MODULE_2__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */] },
    { path: 'locations/:id', component: __WEBPACK_IMPORTED_MODULE_7__location_location_detail_component__["a" /* LocationDetailComponent */] },
    { path: 'locations', component: __WEBPACK_IMPORTED_MODULE_4__locations_locations_component__["a" /* LocationsComponent */] },
    { path: 'ports', component: __WEBPACK_IMPORTED_MODULE_3__ports_ports_component__["a" /* PortsComponent */] },
    {
        path: 'pricing/:version', component: __WEBPACK_IMPORTED_MODULE_8__pricing_pricing_component__["a" /* PricingComponent */],
        children: [
            { path: '', redirectTo: 'commodities', pathMatch: 'full' },
            {
                path: 'commodities', component: __WEBPACK_IMPORTED_MODULE_9__pricing_commodities_commodities_component__["a" /* CommoditiesComponent */], children: [
                    { path: '', redirectTo: 'chart', pathMatch: 'full' },
                    { path: 'chart', component: __WEBPACK_IMPORTED_MODULE_14__pricing_commodities_commodities_chart_commodities_chart_component__["a" /* CommoditiesChartComponent */] },
                    { path: 'editor', component: __WEBPACK_IMPORTED_MODULE_13__pricing_commodities_commodities_editor_commodities_editor_component__["a" /* CommoditiesEditorComponent */] }
                ]
            },
            {
                path: 'currencies', component: __WEBPACK_IMPORTED_MODULE_10__pricing_currencies_currencies_component__["a" /* CurrenciesComponent */], children: [
                    { path: '', redirectTo: 'chart', pathMatch: 'full' },
                    { path: 'chart', component: __WEBPACK_IMPORTED_MODULE_15__pricing_currencies_currencies_chart_currencies_chart_component__["a" /* CurrenciesChartComponent */] },
                    { path: 'editor', component: __WEBPACK_IMPORTED_MODULE_16__pricing_currencies_currencies_editor_currencies_editor_component__["a" /* CurrenciesEditorComponent */] }
                ]
            },
            {
                path: 'basefuel', component: __WEBPACK_IMPORTED_MODULE_11__pricing_basefuel_basefuel_component__["a" /* BaseFuelComponent */], children: [
                    { path: '', redirectTo: 'chart', pathMatch: 'full' },
                    { path: 'chart', component: __WEBPACK_IMPORTED_MODULE_17__pricing_basefuel_base_fuel_chart_base_fuel_chart_component__["a" /* BaseFuelChartComponent */] },
                    { path: 'editor', component: __WEBPACK_IMPORTED_MODULE_18__pricing_basefuel_base_fuel_editor_base_fuel_editor_component__["a" /* BaseFuelEditorComponent */] }
                ]
            },
            {
                path: 'charter', component: __WEBPACK_IMPORTED_MODULE_12__pricing_charter_charter_component__["a" /* CharterComponent */], children: [
                    { path: '', redirectTo: 'chart', pathMatch: 'full' },
                    { path: 'chart', component: __WEBPACK_IMPORTED_MODULE_19__pricing_charter_charter_chart_charter_chart_component__["a" /* CharterChartComponent */] },
                    { path: 'editor', component: __WEBPACK_IMPORTED_MODULE_20__pricing_charter_charter_editor_charter_editor_component__["a" /* CharterEditorComponent */] }
                ]
            }
        ]
    }
];
var AppRoutingModule = (function () {
    function AppRoutingModule() {
    }
    return AppRoutingModule;
}());
AppRoutingModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["RouterModule"].forRoot(routes, { useHash: true })],
        exports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["RouterModule"]]
    })
], AppRoutingModule);

//# sourceMappingURL=app-routing.module.js.map

/***/ }),

/***/ "../../../../../src/app/app.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "h1 {\n  font-size: 1.2em;\n  color: #999;\n  margin-bottom: 0;\n}\nh2 {\n  font-size: 2em;\n  margin-top: 0;\n  padding-top: 0;\n}\nnav a {\n  padding: 5px 10px;\n  text-decoration: none;\n  margin-top: 10px;\n  display: inline-block;\n  background-color: #eee;\n  border-radius: 4px;\n}\nnav a:visited, a:link {\n  color: #607D8B;\n}\nnav a:hover {\n  color: #039be5;\n  background-color: #CFD8DC;\n}\nnav a.active {\n  color: #039be5;\n}\n\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var AppComponent = (function () {
    function AppComponent() {
        this.title = 'Data Navigator';
        this.showNavHeader = true;
    }
    AppComponent.prototype.ngOnInit = function () {
        // subscribe to router event
        console.log(window.location.href);
        if (window.location.href.includes('apiBaseUrl')) {
            this.showNavHeader = false;
        }
    };
    return AppComponent;
}());
AppComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'my-app',
        template: "\n    <div *ngIf=\"showNavHeader\">\n      <h1>{{title}}</h1>\n      <nav>\n          <a routerLink=\"/distances\" routerLinkActive=\"active\">Distances</a>\n          <a routerLink=\"/ports\" routerLinkActive=\"active\">Ports</a>\n          <a routerLink=\"/pricing/latest\" routerLinkActive=\"active\">Pricing</a>\n          <a routerLink=\"/vessels/latest\" routerLinkActive=\"active\">Vessels</a>\n      </nav>\n    </div>\n    <router-outlet></router-outlet>\n ",
        styles: [__webpack_require__("../../../../../src/app/app.component.css")]
    })
], AppComponent);

//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ "../../../../../src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__("../../../platform-browser/@angular/platform-browser.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__ = __webpack_require__("../../../platform-browser/@angular/platform-browser/animations.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_forms__ = __webpack_require__("../../../forms/@angular/forms.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_components_inputtext_inputtext__ = __webpack_require__("../../../../primeng/components/inputtext/inputtext.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_components_inputtext_inputtext___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_primeng_components_inputtext_inputtext__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_primeng_primeng__ = __webpack_require__("../../../../primeng/primeng.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_primeng_primeng___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_primeng_primeng__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__app_routing_module__ = __webpack_require__("../../../../../src/app/app-routing.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9_ng2_handsontable__ = __webpack_require__("../../../../ng2-handsontable/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__generated_ports_locations_api_module__ = __webpack_require__("../../../../../generated/ports-locations/api.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__generated_vessels_api_module__ = __webpack_require__("../../../../../generated/vessels/api.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__distance_matrix_distance_matrix_component__ = __webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__ports_ports_component__ = __webpack_require__("../../../../../src/app/ports/ports.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14__locations_locations_component__ = __webpack_require__("../../../../../src/app/locations/locations.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__location_location_detail_component__ = __webpack_require__("../../../../../src/app/location/location-detail.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_16__generated_ports_locations_variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_17__generated_vessels_variables__ = __webpack_require__("../../../../../generated/vessels/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_18__generated_ports_variables__ = __webpack_require__("../../../../../generated/ports/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_19__pricing_pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_20_primeng_components_chart_chart__ = __webpack_require__("../../../../primeng/components/chart/chart.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_20_primeng_components_chart_chart___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_20_primeng_components_chart_chart__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_21__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_22__pricing_commodities_commodities_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_23__pricing_currencies_currencies_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_24__pricing_charter_charter_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_25__pricing_basefuel_basefuel_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/basefuel.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_26_primeng_components_tabmenu_tabmenu__ = __webpack_require__("../../../../primeng/components/tabmenu/tabmenu.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_26_primeng_components_tabmenu_tabmenu___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_26_primeng_components_tabmenu_tabmenu__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_27_primeng_components_dropdown_dropdown__ = __webpack_require__("../../../../primeng/components/dropdown/dropdown.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_27_primeng_components_dropdown_dropdown___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_27_primeng_components_dropdown_dropdown__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_28_primeng_components_button_button__ = __webpack_require__("../../../../primeng/components/button/button.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_28_primeng_components_button_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_28_primeng_components_button_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_29_primeng_components_multiselect_multiselect__ = __webpack_require__("../../../../primeng/components/multiselect/multiselect.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_29_primeng_components_multiselect_multiselect___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_29_primeng_components_multiselect_multiselect__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_30__pricing_commodities_commodities_editor_commodities_editor_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_31__pricing_curve_editor_curve_editor_component__ = __webpack_require__("../../../../../src/app/pricing/curve-editor/curve-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_32__pricing_commodities_commodities_chart_commodities_chart_component__ = __webpack_require__("../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_33__pricing_currencies_currencies_chart_currencies_chart_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_34__pricing_currencies_currencies_editor_currencies_editor_component__ = __webpack_require__("../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_35__pricing_basefuel_base_fuel_chart_base_fuel_chart_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_36__pricing_basefuel_base_fuel_editor_base_fuel_editor_component__ = __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_37__pricing_charter_charter_editor_charter_editor_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_38__pricing_charter_charter_chart_charter_chart_component__ = __webpack_require__("../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_39__vessels_vessels_component__ = __webpack_require__("../../../../../src/app/vessels/vessels.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_40__vessel_vessel_component__ = __webpack_require__("../../../../../src/app/vessel/vessel.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__ = __webpack_require__("../../../../../src/app/vessels/customCells/customCells.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_42__vessel_vessel_state_attributes_vessel_state_attributes_component__ = __webpack_require__("../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_43_primeng_components_messages_messages__ = __webpack_require__("../../../../primeng/components/messages/messages.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_43_primeng_components_messages_messages___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_43_primeng_components_messages_messages__);
/* unused harmony export getParameterByName */
/* unused harmony export ApiBaseUrlFactory */
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};














































function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.hash);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}
function ApiBaseUrlFactory() {
    if (location.hash.includes('apiBaseUrl')) {
        return getParameterByName('apiBaseUrl');
    }
    return "http://localhost:8090";
}
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_2__angular_core__["NgModule"])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_6__app_component__["a" /* AppComponent */],
            __WEBPACK_IMPORTED_MODULE_12__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */],
            __WEBPACK_IMPORTED_MODULE_13__ports_ports_component__["a" /* PortsComponent */],
            __WEBPACK_IMPORTED_MODULE_14__locations_locations_component__["a" /* LocationsComponent */],
            __WEBPACK_IMPORTED_MODULE_15__location_location_detail_component__["a" /* LocationDetailComponent */],
            __WEBPACK_IMPORTED_MODULE_19__pricing_pricing_component__["a" /* PricingComponent */],
            __WEBPACK_IMPORTED_MODULE_22__pricing_commodities_commodities_component__["a" /* CommoditiesComponent */],
            __WEBPACK_IMPORTED_MODULE_23__pricing_currencies_currencies_component__["a" /* CurrenciesComponent */],
            __WEBPACK_IMPORTED_MODULE_24__pricing_charter_charter_component__["a" /* CharterComponent */],
            __WEBPACK_IMPORTED_MODULE_25__pricing_basefuel_basefuel_component__["a" /* BaseFuelComponent */],
            __WEBPACK_IMPORTED_MODULE_30__pricing_commodities_commodities_editor_commodities_editor_component__["a" /* CommoditiesEditorComponent */],
            __WEBPACK_IMPORTED_MODULE_31__pricing_curve_editor_curve_editor_component__["a" /* CurveEditorComponent */],
            __WEBPACK_IMPORTED_MODULE_32__pricing_commodities_commodities_chart_commodities_chart_component__["a" /* CommoditiesChartComponent */],
            __WEBPACK_IMPORTED_MODULE_33__pricing_currencies_currencies_chart_currencies_chart_component__["a" /* CurrenciesChartComponent */],
            __WEBPACK_IMPORTED_MODULE_34__pricing_currencies_currencies_editor_currencies_editor_component__["a" /* CurrenciesEditorComponent */],
            __WEBPACK_IMPORTED_MODULE_37__pricing_charter_charter_editor_charter_editor_component__["a" /* CharterEditorComponent */],
            __WEBPACK_IMPORTED_MODULE_38__pricing_charter_charter_chart_charter_chart_component__["a" /* CharterChartComponent */],
            __WEBPACK_IMPORTED_MODULE_35__pricing_basefuel_base_fuel_chart_base_fuel_chart_component__["a" /* BaseFuelChartComponent */],
            __WEBPACK_IMPORTED_MODULE_36__pricing_basefuel_base_fuel_editor_base_fuel_editor_component__["a" /* BaseFuelEditorComponent */],
            __WEBPACK_IMPORTED_MODULE_39__vessels_vessels_component__["a" /* VesselsComponent */],
            __WEBPACK_IMPORTED_MODULE_40__vessel_vessel_component__["a" /* VesselComponent */],
            __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["a" /* MyTableCellComponent */],
            __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["b" /* MyTableCellComponentTest */],
            __WEBPACK_IMPORTED_MODULE_42__vessel_vessel_state_attributes_vessel_state_attributes_component__["a" /* VesselStateAttributesComponent */]
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["BrowserModule"],
            __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__["a" /* BrowserAnimationsModule */],
            __WEBPACK_IMPORTED_MODULE_20_primeng_components_chart_chart__["ChartModule"],
            __WEBPACK_IMPORTED_MODULE_3__angular_forms__["FormsModule"],
            __WEBPACK_IMPORTED_MODULE_7__app_routing_module__["a" /* AppRoutingModule */],
            __WEBPACK_IMPORTED_MODULE_8__angular_http__["a" /* HttpModule */],
            __WEBPACK_IMPORTED_MODULE_9_ng2_handsontable__["a" /* HotTableModule */],
            __WEBPACK_IMPORTED_MODULE_10__generated_ports_locations_api_module__["a" /* ApiModule */],
            __WEBPACK_IMPORTED_MODULE_11__generated_vessels_api_module__["a" /* ApiModule */],
            __WEBPACK_IMPORTED_MODULE_26_primeng_components_tabmenu_tabmenu__["TabMenuModule"],
            __WEBPACK_IMPORTED_MODULE_27_primeng_components_dropdown_dropdown__["DropdownModule"],
            __WEBPACK_IMPORTED_MODULE_28_primeng_components_button_button__["ButtonModule"],
            __WEBPACK_IMPORTED_MODULE_4_primeng_components_inputtext_inputtext__["InputTextModule"],
            __WEBPACK_IMPORTED_MODULE_5_primeng_primeng__["SpinnerModule"],
            __WEBPACK_IMPORTED_MODULE_21__angular_common_http__["a" /* HttpClientModule */],
            __WEBPACK_IMPORTED_MODULE_5_primeng_primeng__["FieldsetModule"],
            __WEBPACK_IMPORTED_MODULE_29_primeng_components_multiselect_multiselect__["MultiSelectModule"],
            __WEBPACK_IMPORTED_MODULE_43_primeng_components_messages_messages__["MessagesModule"],
        ],
        entryComponents: [__WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["a" /* MyTableCellComponent */], __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["b" /* MyTableCellComponentTest */]],
        providers: [
            [
                { provide: __WEBPACK_IMPORTED_MODULE_16__generated_ports_locations_variables__["a" /* BASE_PATH */], useFactory: ApiBaseUrlFactory },
                { provide: __WEBPACK_IMPORTED_MODULE_17__generated_vessels_variables__["a" /* BASE_PATH */], useFactory: ApiBaseUrlFactory },
                { provide: __WEBPACK_IMPORTED_MODULE_18__generated_ports_variables__["a" /* BASE_PATH */], useFactory: ApiBaseUrlFactory },
            ], __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["c" /* RouteCellRendererService */], __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["d" /* PortCellRendererService */], __WEBPACK_IMPORTED_MODULE_41__vessels_customCells_customCells_component__["e" /* FuelCellRendererService */],
        ],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_6__app_component__["a" /* AppComponent */]]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".handsontable td, th {\r\n  font-size: 0.7em;\r\n}\r\n\r\n.ui-button{\r\n\r\n\tpadding: 0px !important;\r\n\tborder: 0px !important;\r\n\tmargin: 3px !important;\r\n}\r\n\r\ninput {\r\n\r\n\tborder-top-width: 0px !important;\r\n\tborder-left-width: 0px !important;\r\n\tborder-right-width: 0px !important;\r\n\tborder-bottom-width: 2px !important;\r\n\tmargin-bottom: 12px !important;\r\n}", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.html":
/***/ (function(module, exports) {

module.exports = "<br/>\n<label>From:</label> <input type=\"text\" (input)=\"fromChange($event.target.value)\" />\n<label>To:</label> <input type=\"text\" (input)=\"toChange($event.target.value)\" />\n<button pButton (click)=\"showLocations()\" class=\"ui-button-secondary\" icon=\"fa-edit\" type=\"submit\" label=\"Locations\"></button>\n<br/>\n<hotTable [data]=\"data\"\n          (after-change)=\"afterChange($event)\"\n          (after-on-cell-mouse-down)=\"afterOnCellMouseDown($event)\"\n          [colHeaders]=\"colHeaders\"\n          [options]=\"options\"\n          [colWidths]=\"colWidths\">\n</hotTable>\n"

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__ = __webpack_require__("../../../../../generated/ports-locations/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__ = __webpack_require__("../../../../rxjs/add/operator/toPromise.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_rxjs_add_operator_toPromise__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DistanceMatrixComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var DistanceMatrixComponent = (function () {
    function DistanceMatrixComponent(router, route, locationsService, distancesService) {
        var _this = this;
        this.router = router;
        this.route = route;
        this.locationsService = locationsService;
        this.distancesService = distancesService;
        this.data = new Array();
        this.colHeaders = new Array();
        this.locations = new Array();
        // name --> mmxId
        this.locationMap = new Map();
        if (route.snapshot.paramMap.has("version")) {
            this.version = route.snapshot.paramMap.get("version");
        }
        this.options = {
            height: 700,
            rowHeaders: new Array(),
            rowHeaderWidth: 180,
            stretchH: 'all',
            autoColumnSize: true,
            columnSorting: true,
            contextMenu: true,
            className: 'htCenter htMiddle',
            afterChange: function (changes) {
                if (changes == null) {
                    // prevent update calls when building table
                    return;
                }
                _this.updateDistance(_this.locationForRow(changes[0][0]), _this.locationForColumn(changes[0][1]), changes[0][3]);
            }
        };
    }
    DistanceMatrixComponent.prototype.ngOnInit = function () {
        this.fetchUpstream();
    };
    DistanceMatrixComponent.prototype.fetchUpstream = function () {
        var _this = this;
        this.locationsService.getLocationsUsingGET(this.version, undefined).toPromise()
            .then(function (locationns) {
            _this.locations = locationns.filter(function (l) { return l.virtual == false; });
            _this.data = new Array(_this.locations.length);
            for (var _i = 0; _i < _this.data.length; _i++) {
                _this.data[_i] = new Array(_this.locations.length);
            }
            _this.locations.filter(function (l) { return l.virtual == false; }).forEach(function (l) {
                _this.locationMap.set(l.name, l.mmxId);
                _this.colHeaders.push(l.name);
                _this.options.rowHeaders.push(l.name);
            });
        })
            .catch(function (error) { return console.log(error); });
        this.distancesService.getDistanceMatrixUsingGET(undefined, this.version, undefined).toPromise()
            .then(function (distances) {
            _this.distances = distances;
            for (var i = 0; i < _this.colHeaders.length; i++) {
                _this.data[i] = new Array();
                for (var j = 0; j < _this.colHeaders.length; j++) {
                    _this.data[i][j] = _this.distances[_this.locationMap.get(_this.options.rowHeaders[i])][_this.locationMap.get(_this.colHeaders[j])];
                }
            }
        });
    };
    DistanceMatrixComponent.prototype.locationForRow = function (row) {
        var _this = this;
        return this.locations.find(function (p) { return p.name === _this.options.rowHeaders[row.toString()]; });
    };
    DistanceMatrixComponent.prototype.locationForColumn = function (column) {
        var _this = this;
        return this.locations.find(function (p) { return p.name === _this.colHeaders[column.toString()]; });
    };
    DistanceMatrixComponent.prototype.updateDistance = function (from, to, distance) {
        this.distancesService.updateDistanceUsingPUT(from.mmxId, to.mmxId, { distance: distance, provider: "Web UI" }).toPromise()
            .then(function (result) { return console.log("successfully updated"); })
            .catch(function (error) { return console.log(error); });
        console.log("the update: " + from.name + " -> " + to.name + ": " + distance);
    };
    DistanceMatrixComponent.prototype.filterColumn = function (searchString) {
        var filteredLocations = this.locations.filter(function (p) { return p.name.toLowerCase().startsWith(searchString.toLowerCase()); });
        // let filteredColHeaders = this.colHeaders.filter(e => e.startsWith(searchString))
        var filteredData = new Array();
        for (var i = 0; i < this.options.rowHeaders.length; i++) {
            filteredData[i] = new Array();
            for (var j = 0; j < filteredLocations.length; j++) {
                filteredData[i][j] = this.distances[this.locationMap.get(this.options.rowHeaders[i])][filteredLocations[j].mmxId];
            }
        }
        this.colHeaders = filteredLocations.map(function (p) { return p.name; });
        this.data = filteredData;
        this.options = Object.assign({}, this.options);
    };
    DistanceMatrixComponent.prototype.filterRow = function (searchString) {
        var filteredLocations = this.locations.filter(function (p) { return p.name.toLowerCase().startsWith(searchString.toLowerCase()); });
        // let filteredColHeaders = this.colHeaders.filter(e => e.startsWith(searchString))
        var filteredData = new Array();
        for (var i = 0; i < filteredLocations.length; i++) {
            filteredData[i] = new Array();
            for (var j = 0; j < this.colHeaders.length; j++) {
                filteredData[i][j] = this.distances[filteredLocations[i].mmxId][this.locationMap.get(this.colHeaders[j])];
            }
        }
        this.options.rowHeaders = filteredLocations.map(function (p) { return p.name; });
        this.data = filteredData;
        this.options = Object.assign({}, this.options);
    };
    DistanceMatrixComponent.prototype.fromChange = function (seachString) {
        this.filterRow(seachString);
    };
    DistanceMatrixComponent.prototype.toChange = function (seachString) {
        this.filterColumn(seachString);
    };
    DistanceMatrixComponent.prototype.showLocations = function () {
        this.router.navigate(['/locations']);
    };
    return DistanceMatrixComponent;
}());
DistanceMatrixComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-distance-matrix',
        template: __webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.html"),
        styles: [__webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.css")],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["Router"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["Router"]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["LocationsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["LocationsService"]) === "function" && _c || Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["DistancesService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["DistancesService"]) === "function" && _d || Object])
], DistanceMatrixComponent);

var _a, _b, _c, _d;
//# sourceMappingURL=distance-matrix.component.js.map

/***/ }),

/***/ "../../../../../src/app/location/location-detail.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/location/location-detail.component.html":
/***/ (function(module, exports) {

module.exports = "<div *ngIf=\"hero\">\n    <h2>{{hero.name}} details!</h2>\n    <div><label>id: </label>{{hero.id}}</div>\n    <div>\n      <label>name: </label>\n      <input [(ngModel)]=\"hero.name\" placeholder=\"name\"/>\n    </div>\n    <button (click)=\"goBack()\">Back</button>\n    <button (click)=\"save()\">Save</button>\n</div>"

/***/ }),

/***/ "../../../../../src/app/location/location-detail.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__generated_ports_locations_index__ = __webpack_require__("../../../../../generated/ports-locations/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap__ = __webpack_require__("../../../../rxjs/add/operator/switchMap.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LocationDetailComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var LocationDetailComponent = (function () {
    function LocationDetailComponent(route, angularLocation, locationsService) {
        this.route = route;
        this.angularLocation = angularLocation;
        this.locationsService = locationsService;
    }
    LocationDetailComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.paramMap
            .switchMap(function (params) { return _this.locationsService.getLocationUsingGET(params.get('id')); })
            .subscribe(function (port) { return _this.port = port; });
    };
    LocationDetailComponent.prototype.goBack = function () {
        this.angularLocation.back();
    };
    LocationDetailComponent.prototype.save = function () {
        //
    };
    return LocationDetailComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__generated_ports_locations_index__["Location"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__generated_ports_locations_index__["Location"]) === "function" && _a || Object)
], LocationDetailComponent.prototype, "port", void 0);
LocationDetailComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-location-detail',
        template: __webpack_require__("../../../../../src/app/location/location-detail.component.html"),
        styles: [__webpack_require__("../../../../../src/app/location/location-detail.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_router__["ActivatedRoute"]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_2__angular_common__["Location"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_common__["Location"]) === "function" && _c || Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_3__generated_ports_locations_index__["LocationsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__generated_ports_locations_index__["LocationsService"]) === "function" && _d || Object])
], LocationDetailComponent);

var _a, _b, _c, _d;
//# sourceMappingURL=location-detail.component.js.map

/***/ }),

/***/ "../../../../../src/app/locations/locations.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "selected {\n    background-color: #CFD8DC !important;\n    color: white;\n  }\n  .ports {\n    margin: 0 0 2em 0;\n    list-style-type: none;\n    padding: 0;\n    width: 20em;\n  }\n  .ports li {\n    cursor: pointer;\n    position: relative;\n    left: 0;\n    background-color: #EEE;\n    margin: .5em;\n    padding: .3em 0;\n    height: 1.6em;\n    border-radius: 4px;\n  }\n  .ports li:hover {\n    color: #607D8B;\n    background-color: #DDD;\n    left: .1em;\n  }\n  .ports li.selected:hover {\n    background-color: #BBD8DC !important;\n    color: white;\n  }\n  .ports .text {\n    position: relative;\n    top: -3px;\n  }\n  .ports .badge {\n    display: inline-block;\n    font-size: small;\n    color: white;\n    padding: 0.8em 0.7em 0 0.7em;\n    background-color: #607D8B;\n    line-height: 1em;\n    position: relative;\n    left: -1px;\n    top: -4px;\n    height: 1.8em;\n    width: 8em;\n    margin-right: .8em;\n    border-radius: 4px 0 0 4px;\n  }\n  button {\n    font-family: Arial;\n    background-color: #eee;\n    border: none;\n    padding: 5px 10px;\n    border-radius: 4px;\n    cursor: pointer;\n    cursor: hand;\n  }\n  button:hover {\n    background-color: #cfd8dc;\n  }\n\n  button.delete {\n    float:right;\n    margin-top: 2px;\n    margin-right: .8em;\n    background-color: gray !important;\n    color:white;\n  }", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/locations/locations.component.html":
/***/ (function(module, exports) {

module.exports = "<h2>Locations</h2>\n<ul class=\"ports\">\n    <li *ngFor=\"let port of ports\" (click)=\"gotoDetail(port)\">\n      <span class=\"badge\">{{port.country}} {{port.mmxId}}</span> {{port.name}}\n    </li>\n</ul>\n"

/***/ }),

/***/ "../../../../../src/app/locations/locations.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__ = __webpack_require__("../../../../../generated/ports-locations/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LocationsComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var LocationsComponent = (function () {
    function LocationsComponent(router, portsService) {
        this.router = router;
        this.portsService = portsService;
    }
    LocationsComponent.prototype.ngOnInit = function () {
        this.getPorts();
    };
    LocationsComponent.prototype.getPorts = function () {
        var _this = this;
        this.portsService.getLocationsUsingGET().toPromise()
            .then(function (ports) { return _this.ports = ports; })
            .catch(function (error) { return console.log(error); });
    };
    LocationsComponent.prototype.gotoDetail = function (selectedLocation) {
        this.router.navigate(['/locations', selectedLocation.mmxId]);
    };
    return LocationsComponent;
}());
LocationsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-locations',
        template: __webpack_require__("../../../../../src/app/locations/locations.component.html"),
        styles: [__webpack_require__("../../../../../src/app/locations/locations.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["LocationsService"]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__angular_router__["Router"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_router__["Router"]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["LocationsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_locations_index__["LocationsService"]) === "function" && _b || Object])
], LocationsComponent);

var _a, _b;
//# sourceMappingURL=locations.component.js.map

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "\n.handsontable td {\n  white-space: normal;\n  overflow:visible !important;\n}\n\n\n.handsontable td, th {\n  font-size: 0.7em;\n}\nbutton {\n  font-family: Arial;\n  background-color: #eee;\n  border: none;\n  padding: 5px 10px;\n  border-radius: 4px;\n  cursor: pointer;\n  cursor: hand;\n}\n\nbutton:hover {\n  background-color: #cfd8dc;\n}\n\nbutton.delete {\n  float:right;\n  margin-top: 2px;\n  margin-right: .8em;\n  background-color: gray !important;\n  color:white;\n}\n\np-multiSelect {\n  width: 100%;\n}\n\nmy-table-cell {\n  white-space: normal;\n  overflow-y: visible !important;\n  overflow-x: visible !important;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.html":
/***/ (function(module, exports) {

module.exports = "<p-messages  [(value)]=\"msgs\"></p-messages>\n\n<hotTable [colHeaders]=\"colHeaders\"\n  [columns]=\"columns\"\n  [options]=\"options\">\n</hotTable>\n\n<br />\n\n<button pButton (click)=\"savePorts()\" class=\"ui-button-secondary\" icon=\"fa-save\" type=\"submit\" label=\"Save\"></button>\n\n<div #templateContainer\n    [hidden]=\"true\">\n</div>\n"

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_ports_index__ = __webpack_require__("../../../../../generated/ports/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_index__ = __webpack_require__("../../../../../generated/ports-locations/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice__ = __webpack_require__("../../../../primeng/components/common/messageservice.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__vessels_customCells_customCells_component__ = __webpack_require__("../../../../../src/app/vessels/customCells/customCells.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6_jquery__ = __webpack_require__("../../../../jquery/dist/jquery.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6_jquery___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6_jquery__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PortsComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};











var PortsComponent = (function () {
    function PortsComponent(router, portService, locationsService, distancesService, messageService, factoryResolver, portCellRendererService) {
        var _this = this;
        this.router = router;
        this.portService = portService;
        this.locationsService = locationsService;
        this.distancesService = distancesService;
        this.messageService = messageService;
        this.factoryResolver = factoryResolver;
        this.portCellRendererService = portCellRendererService;
        this.isValid = true;
        this.msgs = [];
        this.locations = new Array();
        this.capabilities = ['LOAD', 'DISCHARGE', 'DRYDOCK', 'MAINTENANCE'];
        this.cellCapabilitiesTemplateComponents = {};
        this.portCellRendererService.portCellRendererEvent.subscribe(function (data) {
            console.log(data);
            _this.ports[data.index]['capabilities'] = data.data;
        });
    }
    PortsComponent.prototype.ngOnInit = function () {
        this.getPorts();
    };
    PortsComponent.prototype.getPorts = function () {
        var _this = this;
        this.portService.fetchAllUsingGET().toPromise()
            .then(function (ports) {
            _this.ports = ports;
            _this.distancesService.getVersionsUsingGET().toPromise()
                .then(function (versions) {
                if (versions.length > 0) {
                    _this.locationsService.getLocationsUsingGET(versions[0].identifier).toPromise()
                        .then(function (locations) {
                        _this.locations = locations;
                        _this.createSpreadsheet();
                    })
                        .catch(function (error) {
                        _this.msgs = [];
                        _this.msgs.push({ severity: 'error', summary: 'Can\'t get location for the latest version', detail: versions[0] + ": " + error });
                        console.log(error);
                    });
                }
            })
                .catch(function (error) {
                _this.msgs = [];
                _this.msgs.push({ severity: 'error', summary: 'Error accessing the Distance service\'s versions', detail: error });
                console.log(error);
            });
        })
            .catch(function (error) {
            _this.msgs = [];
            _this.msgs.push({ severity: 'error', summary: 'Error fetching the port', detail: error });
            console.log(error);
            _this.ports = [];
            _this.createSpreadsheet();
        });
    };
    PortsComponent.prototype.createEmptyPort = function () {
        var port = { 'mmxId': '',
            'locationMmxId': '',
            'defaultStartTime': null,
            'allowCooldown': null,
            'berths': null,
            'capabilities': [],
            'cvValue': null,
            'dischargeDuration': null,
            'loadDuration': null,
            'maxCvValue': null,
            'minCvValue': null,
        };
        return port;
    };
    PortsComponent.prototype.createSpreadsheet = function () {
        var _this = this;
        var source = this.locations
            .filter(function (l) { return l.virtual == false; })
            .map(function (l) {
            var res = [];
            res.push(l.name);
            res.push.apply(res, l.aliases);
            return res;
        })
            .reduce(function (a, b) { return a.concat(b); }, []);
        this.columns = [
            { data: "locationMmxId", title: 'Location Id', type: 'autocomplete',
                source: source,
                strict: true,
                allowInvalid: false,
                width: 180 },
            { data: 'defaultStartTime', title: 'Start Time', type: 'time', timeFormat: 'h:mm', correctFormat: true },
            { data: 'dischargeDuration', title: 'Discharge duration (hrs)', type: 'numeric' },
            { data: 'loadDuration', title: 'Load duration (hrs)', type: 'numeric' },
            { data: 'cvValue', title: 'CV (mmBTU)', type: 'numeric' },
            { data: 'maxCvValue', title: 'Max CV (calorie)', type: 'numeric' },
            { data: 'minCvValue', title: 'Min CV (mmBTU)', type: 'numeric' },
            { data: 'allowCooldown', title: 'Allow Cooldown', type: 'checkbox' },
            { data: 'berths', title: 'Berths', type: 'numeric' },
            { data: 'capabilities', title: 'Capabilities', renderer: this.rendererInaccessiblePortLinkCell.bind(this), readOnly: true, },
        ];
        this.rowHeaders = [];
        this.rowHeaders = this.ports.map(function (p) { return p.mmxId; });
        this.options = {
            data: this.ports,
            rowHeaders: this.rowHeaders,
            colHeaders: this.colHeaders,
            columns: this.columns,
            rowHeaderWidth: 150,
            autoColumnSize: true,
            manualColumnMove: true,
            manualRowMove: true,
            manualColumnResize: true,
            bindRowsWithHeaders: true,
            manualRowResize: true,
            contextMenu: true,
            search: true,
            columnSorting: true,
            sortIndicator: true,
            undo: true,
            afterValidate: function (isValid, value, row, prop, source) {
                _this.isValid = isValid;
            },
        };
        this.cellComponentFactory = this.factoryResolver
            .resolveComponentFactory(__WEBPACK_IMPORTED_MODULE_5__vessels_customCells_customCells_component__["a" /* MyTableCellComponent */]);
    };
    PortsComponent.prototype.rendererInaccessiblePortLinkCell = function (instance, td, row, col, columnKey, data, cellProperties) {
        var component = this.cellCapabilitiesTemplateComponents[row];
        if (!component) {
            console.log(this.templateContainer);
            console.log(this.cellComponentFactory);
            component = this.templateContainer
                .createComponent(this.cellComponentFactory);
            var port = this.ports[row];
            if (port['capabilities'] == null || port['capabilities'] == undefined) {
                port['capabilities'] = [];
            }
            component.instance.selected = port['capabilities'];
            Object.assign(component.instance, { index: row, componentType: 'port' });
            component.instance.setSelection(this.capabilities);
            component.changeDetectorRef.detectChanges();
            this.cellCapabilitiesTemplateComponents[row] = component;
        }
        __WEBPACK_IMPORTED_MODULE_6_jquery__(td).html(component.location.nativeElement);
    };
    ;
    PortsComponent.prototype.isEmpty = function (obj) {
        return Object.values(obj).every(function (x) { return (x == false || x === "" || x == null); });
    };
    PortsComponent.prototype.savePorts = function () {
        var _this = this;
        if (!this.isValid) {
            this.msgs = [];
            this.msgs.push({ severity: 'warn', summary: 'Can\'t save, invalid data in spreadsheet !', detail: '' });
            return;
        }
        else {
            this.msgs = [];
        }
        var ports = this.ports.map(function (p) {
            return Object.assign(_this.createEmptyPort(), p);
        });
        ports = ports.filter(function (p) {
            return !_this.isEmpty(p);
        });
        console.log(ports);
        this.portService.bulkInsertUsingPOST(ports).toPromise()
            .then(function () { console.log('Ok !'); })
            .catch(function () {
            _this.msgs = [];
            _this.msgs.push({ severity: 'error', summary: 'Can\'t save version !', detail: '' });
            console.log('Not Ok!');
        });
    };
    return PortsComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('templateContainer', { read: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"] }),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"]) === "function" && _a || Object)
], PortsComponent.prototype, "templateContainer", void 0);
PortsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-ports',
        template: __webpack_require__("../../../../../src/app/ports/ports.component.html"),
        styles: [__webpack_require__("../../../../../src/app/ports/ports.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__generated_ports_index__["PortService"], __WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice__["MessageService"]],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None
    }),
    __metadata("design:paramtypes", [typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["Router"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["Router"]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_index__["PortService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_index__["PortService"]) === "function" && _c || Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_index__["LocationsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_index__["LocationsService"]) === "function" && _d || Object, typeof (_e = typeof __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_index__["DistancesService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_index__["DistancesService"]) === "function" && _e || Object, typeof (_f = typeof __WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice__["MessageService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4_primeng_components_common_messageservice__["MessageService"]) === "function" && _f || Object, typeof (_g = typeof __WEBPACK_IMPORTED_MODULE_0__angular_core__["ComponentFactoryResolver"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_0__angular_core__["ComponentFactoryResolver"]) === "function" && _g || Object, typeof (_h = typeof __WEBPACK_IMPORTED_MODULE_5__vessels_customCells_customCells_component__["d" /* PortCellRendererService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__vessels_customCells_customCells_component__["d" /* PortCellRendererService */]) === "function" && _h || Object])
], PortsComponent);

var _a, _b, _c, _d, _e, _f, _g, _h;
//# sourceMappingURL=ports.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/@angular/common/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_variables__ = __webpack_require__("../../../../../generated/ports-locations/variables.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PricingService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};



var PricingService = (function () {
    function PricingService(http, basePath) {
        this.http = http;
        this.basePath = "http://localhost:8096";
        if (basePath) {
            this.basePath = basePath;
        }
    }
    PricingService.prototype.getCurrencies = function (version) {
        return this.getSpecific(this.basePath + "/currencies", version);
    };
    PricingService.prototype.getCommodities = function (version) {
        return this.getSpecific(this.basePath + "/commodities", version);
    };
    PricingService.prototype.getBaseFuel = function (version) {
        return this.getSpecific(this.basePath + "/basefuel", version);
    };
    PricingService.prototype.getCharter = function (version) {
        return this.getSpecific(this.basePath + "/charter", version);
    };
    PricingService.prototype.getSpecific = function (endPoint, version) {
        var _this = this;
        var urlParams = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]();
        if (version != undefined) {
            urlParams = urlParams.append('v', version);
        }
        return this.http.get(endPoint, { params: urlParams }).toPromise().then(function (response) {
            if (response === undefined || response === null || response.length === 0) {
                return [];
            }
            return _this.getCurves(response.map(function (e) { return e.name; }), version);
        });
    };
    /**
     * Bulk retrival of curves
     * @param curves
     */
    PricingService.prototype.getCurves = function (curves, version) {
        var urlParams = curves.reduce(function (params, element) { return params.append("curve", element); }, new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]());
        if (version != undefined) {
            urlParams = urlParams.append('v', version);
        }
        return this.http.get(this.basePath + "/bulk", {
            params: urlParams
        }).toPromise();
    };
    return PricingService;
}());
PricingService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Optional"])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Inject"])(__WEBPACK_IMPORTED_MODULE_2__generated_ports_locations_variables__["a" /* BASE_PATH */])),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["d" /* HttpClient */]) === "function" && _a || Object, String])
], PricingService);

var _a;
//# sourceMappingURL=pricing.service.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.html":
/***/ (function(module, exports) {

module.exports = "<p-dropdown [options]=\"currencies\" [(ngModel)]=\"selectedCurrency\" (onChange)=\"currencyChange()\"></p-dropdown>\n<p-chart type=\"line\" [data]=\"data\"></p-chart>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BaseFuelChartComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var BaseFuelChartComponent = (function () {
    function BaseFuelChartComponent(pricingService, route) {
        this.pricingService = pricingService;
        this.route = route;
        this.unfilteredDataSets = new Array();
        this.currencies = new Array();
        var root = route.snapshot.root;
        var pricingRoute = root.children.find(function (e) { return e.paramMap.keys.indexOf("version") > -1; });
        if (pricingRoute != undefined && pricingRoute.params.version != "latest") {
            this.version = pricingRoute.params.version;
        }
        else {
            // leave undefined
        }
        this.data = {
            labels: [],
            datasets: []
        };
    }
    BaseFuelChartComponent.prototype.ngOnInit = function () {
        this.colorStack = __WEBPACK_IMPORTED_MODULE_2__pricing_component__["a" /* PricingComponent */].COLORS.slice();
        this.loadCurves();
    };
    BaseFuelChartComponent.prototype.loadCurves = function () {
        var _this = this;
        this.pricingService.getBaseFuel(this.version)
            .then(function (result) {
            _this.data.labels = result.curves[0].curve.map(function (e) { return e.date; });
            result.curves.forEach(function (element) {
                // TODO: expression curves are ignored for now. Need to be handled on the server
                if (element['@class'] === ".ExpressionCurve") {
                    return;
                }
                _this.unfilteredDataSets.push({
                    currency: element.currency,
                    label: element.name,
                    data: element.curve.map(function (e) { return e.value; }),
                    fill: false,
                    borderColor: _this.colorStack.pop()
                });
            });
            _this.getCurrencies(result.curves);
        });
    };
    BaseFuelChartComponent.prototype.currencyChange = function () {
        this.filterCurrencies(this.selectedCurrency);
    };
    BaseFuelChartComponent.prototype.getCurrencies = function (curves) {
        var currencies = new Set();
        curves.forEach(function (element) {
            currencies.add(element.currency);
        });
        var currencyList = Array.from(currencies);
        for (var i = 0; i < currencyList.length; i++) {
            if (i == 0) {
                this.selectedCurrency = currencyList[i];
                this.filterCurrencies(currencyList[i]);
            }
            this.currencies.push({ label: currencyList[i], value: currencyList[i] });
        }
    };
    BaseFuelChartComponent.prototype.filterCurrencies = function (currency) {
        this.data = {
            datasets: this.unfilteredDataSets.filter(function (e) { return e.currency === currency; }),
            labels: this.data.labels
        };
    };
    return BaseFuelChartComponent;
}());
BaseFuelChartComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-base-fuel-chart',
        template: __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-chart/base-fuel-chart.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"]) === "function" && _b || Object])
], BaseFuelChartComponent);

var _a, _b;
//# sourceMappingURL=base-fuel-chart.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.html":
/***/ (function(module, exports) {

module.exports = "<curve-editor [curves]=\"curves\" (afterChange)=\"afterChange($event)\"></curve-editor>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BaseFuelEditorComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var BaseFuelEditorComponent = (function () {
    function BaseFuelEditorComponent(pricingService) {
        this.pricingService = pricingService;
        this.options = { dragData: true };
    }
    BaseFuelEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.pricingService.getBaseFuel().then(function (basefuels) {
            _this.curves = basefuels.curves;
        });
    };
    BaseFuelEditorComponent.prototype.afterChange = function (changes) {
        console.log("afterChange called!");
    };
    return BaseFuelEditorComponent;
}());
BaseFuelEditorComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-base-fuel-editor',
        template: __webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/basefuel/base-fuel-editor/base-fuel-editor.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object])
], BaseFuelEditorComponent);

var _a;
//# sourceMappingURL=base-fuel-editor.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/basefuel.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/basefuel.component.html":
/***/ (function(module, exports) {

module.exports = "<button pButton type=\"button\" [routerLink]=\"['chart']\" icon=\"fa-line-chart\"  class=\"ui-button-secondary\"></button>\n<button pButton type=\"button\" [routerLink]=\"['editor']\" icon=\"fa-table\"  class=\"ui-button-secondary\"></button>\n<router-outlet></router-outlet>"

/***/ }),

/***/ "../../../../../src/app/pricing/basefuel/basefuel.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BaseFuelComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var BaseFuelComponent = (function () {
    function BaseFuelComponent() {
    }
    BaseFuelComponent.prototype.ngOnInit = function () {
    };
    return BaseFuelComponent;
}());
BaseFuelComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-basefuel',
        template: __webpack_require__("../../../../../src/app/pricing/basefuel/basefuel.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/basefuel/basefuel.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [])
], BaseFuelComponent);

//# sourceMappingURL=basefuel.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.html":
/***/ (function(module, exports) {

module.exports = "<p-dropdown [options]=\"currencies\" [(ngModel)]=\"selectedCurrency\" (onChange)=\"currencyChange()\"></p-dropdown>\n<p-chart type=\"line\" [data]=\"data\"></p-chart>"

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CharterChartComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var CharterChartComponent = (function () {
    function CharterChartComponent(pricingService, route) {
        this.pricingService = pricingService;
        this.route = route;
        this.unfilteredDataSets = new Array();
        this.currencies = new Array();
        var root = route.snapshot.root;
        var pricingRoute = root.children.find(function (e) { return e.paramMap.keys.indexOf("version") > -1; });
        if (pricingRoute != undefined && pricingRoute.params.version != "latest") {
            this.version = pricingRoute.params.version;
        }
        else {
            // leave undefined
        }
        this.data = {
            labels: [],
            datasets: []
        };
    }
    CharterChartComponent.prototype.ngOnInit = function () {
        this.colorStack = __WEBPACK_IMPORTED_MODULE_2__pricing_component__["a" /* PricingComponent */].COLORS.slice();
        this.loadCurves();
    };
    CharterChartComponent.prototype.loadCurves = function () {
        var _this = this;
        this.pricingService.getCharter(this.version)
            .then(function (result) {
            console.log(result);
            if (result.length > 0) {
                _this.data.labels = result.curves[0].curve.map(function (e) { return e.date; });
                result.curves.forEach(function (element) {
                    // TODO: expression curves are ignored for now. Need to be handled on the server
                    if (element['@name'] === "ExpressionCurve") {
                        return;
                    }
                    _this.unfilteredDataSets.push({
                        currency: element.currency,
                        label: element.name,
                        data: element.curve.map(function (e) { return e.value; }),
                        fill: false,
                        borderColor: _this.colorStack.pop()
                    });
                });
                _this.getCurrencies(result.curves);
            }
        });
    };
    CharterChartComponent.prototype.currencyChange = function () {
        this.filterCurrencies(this.selectedCurrency);
    };
    CharterChartComponent.prototype.getCurrencies = function (curves) {
        var currencies = new Set();
        curves.forEach(function (element) {
            currencies.add(element.currency);
        });
        var currencyList = Array.from(currencies);
        for (var i = 0; i < currencyList.length; i++) {
            if (i == 0) {
                this.selectedCurrency = currencyList[i];
                this.filterCurrencies(currencyList[i]);
            }
            this.currencies.push({ label: currencyList[i], value: currencyList[i] });
        }
    };
    CharterChartComponent.prototype.filterCurrencies = function (currency) {
        this.data = {
            datasets: this.unfilteredDataSets.filter(function (e) { return e.currency === currency; }),
            labels: this.data.labels
        };
    };
    return CharterChartComponent;
}());
CharterChartComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-charter-chart',
        template: __webpack_require__("../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/charter/charter-chart/charter-chart.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"]) === "function" && _b || Object])
], CharterChartComponent);

var _a, _b;
//# sourceMappingURL=charter-chart.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.html":
/***/ (function(module, exports) {

module.exports = "<curve-editor [curves]=\"curves\" (afterChange)=\"afterChange($event)\"></curve-editor>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CharterEditorComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CharterEditorComponent = (function () {
    function CharterEditorComponent(pricingService) {
        this.pricingService = pricingService;
    }
    CharterEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.pricingService.getCharter().then(function (chartersRate) {
            _this.curves = chartersRate.curves;
        });
    };
    CharterEditorComponent.prototype.afterChange = function (changes) {
        console.log("afterChange called!");
    };
    return CharterEditorComponent;
}());
CharterEditorComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-charter-editor',
        template: __webpack_require__("../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/charter/charter-editor/charter-editor.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object])
], CharterEditorComponent);

var _a;
//# sourceMappingURL=charter-editor.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter.component.html":
/***/ (function(module, exports) {

module.exports = "<button pButton type=\"button\" [routerLink]=\"['chart']\" icon=\"fa-line-chart\"  class=\"ui-button-secondary\"></button>\n<button pButton type=\"button\" [routerLink]=\"['editor']\" icon=\"fa-table\"  class=\"ui-button-secondary\"></button>\n<router-outlet></router-outlet>"

/***/ }),

/***/ "../../../../../src/app/pricing/charter/charter.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CharterComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CharterComponent = (function () {
    function CharterComponent() {
    }
    CharterComponent.prototype.ngOnInit = function () {
    };
    return CharterComponent;
}());
CharterComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-charter',
        template: __webpack_require__("../../../../../src/app/pricing/charter/charter.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/charter/charter.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [])
], CharterComponent);

//# sourceMappingURL=charter.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.html":
/***/ (function(module, exports) {

module.exports = "<p-dropdown [options]=\"currencies\" [(ngModel)]=\"selectedCurrency\" (onChange)=\"currencyChange()\"></p-dropdown>\n<p-chart type=\"line\" [data]=\"data\"></p-chart>"

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CommoditiesChartComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var CommoditiesChartComponent = (function () {
    function CommoditiesChartComponent(pricingService, route) {
        this.pricingService = pricingService;
        this.route = route;
        this.unfilteredDataSets = new Array();
        this.currencies = new Array();
        var root = route.snapshot.root;
        var pricingRoute = root.children.find(function (e) { return e.paramMap.keys.indexOf("version") > -1; });
        if (pricingRoute != undefined && pricingRoute.params.version != "latest") {
            this.version = pricingRoute.params.version;
            console.log("version: " + this.version);
        }
        else {
            console.log("latest version");
            // leave undefined
        }
        this.data = {
            labels: [],
            datasets: []
        };
    }
    CommoditiesChartComponent.prototype.ngOnInit = function () {
        this.colorStack = __WEBPACK_IMPORTED_MODULE_2__pricing_component__["a" /* PricingComponent */].COLORS.slice();
        this.loadCurves();
    };
    CommoditiesChartComponent.prototype.loadCurves = function () {
        var _this = this;
        this.pricingService.getCommodities(this.version)
            .then(function (result) {
            _this.data.labels = result.curves[0].curve.map(function (e) { return e.date; });
            result.curves.forEach(function (element) {
                // TODO: expression curves are ignored for now. Need to be handled on the server
                if (element['@name'] === "ExpressionCurve") {
                    return;
                }
                _this.unfilteredDataSets.push({
                    currency: element.currency,
                    label: element.name,
                    data: element.curve.map(function (e) { return e.value; }),
                    fill: false,
                    borderColor: _this.colorStack.pop()
                });
            });
            _this.getCurrencies(result.curves);
        });
    };
    CommoditiesChartComponent.prototype.currencyChange = function () {
        this.filterCurrencies(this.selectedCurrency);
    };
    CommoditiesChartComponent.prototype.getCurrencies = function (curves) {
        var currencies = new Set();
        curves.forEach(function (element) {
            currencies.add(element.currency);
        });
        var currencyList = Array.from(currencies);
        for (var i = 0; i < currencyList.length; i++) {
            if (i == 0) {
                this.selectedCurrency = currencyList[i];
                this.filterCurrencies(currencyList[i]);
            }
            this.currencies.push({ label: currencyList[i], value: currencyList[i] });
        }
    };
    CommoditiesChartComponent.prototype.filterCurrencies = function (currency) {
        this.data = {
            datasets: this.unfilteredDataSets.filter(function (e) { return e.currency === currency; }),
            labels: this.data.labels
        };
    };
    return CommoditiesChartComponent;
}());
CommoditiesChartComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-commodities-chart',
        template: __webpack_require__("../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/commodities/commodities-chart/commodities-chart.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"]) === "function" && _b || Object])
], CommoditiesChartComponent);

var _a, _b;
//# sourceMappingURL=commodities-chart.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.html":
/***/ (function(module, exports) {

module.exports = "<curve-editor [curves]=\"curves\" (afterChange)=\"afterChange($event)\"></curve-editor>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CommoditiesEditorComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CommoditiesEditorComponent = (function () {
    function CommoditiesEditorComponent(pricingService) {
        this.pricingService = pricingService;
    }
    CommoditiesEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.pricingService.getCommodities().then(function (commodities) {
            _this.curves = commodities.curves;
        });
    };
    CommoditiesEditorComponent.prototype.afterChange = function (changes) {
        console.log("afterChange called!");
    };
    return CommoditiesEditorComponent;
}());
CommoditiesEditorComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-commodities-editor',
        template: __webpack_require__("../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/commodities/commodities-editor/commodities-editor.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object])
], CommoditiesEditorComponent);

var _a;
//# sourceMappingURL=commodities-editor.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities.component.html":
/***/ (function(module, exports) {

module.exports = "<button pButton type=\"button\" [routerLink]=\"['chart']\" icon=\"fa-line-chart\"  class=\"ui-button-secondary\"></button>\n<button pButton type=\"button\" [routerLink]=\"['editor']\" icon=\"fa-table\"  class=\"ui-button-secondary\"></button>\n<router-outlet></router-outlet>"

/***/ }),

/***/ "../../../../../src/app/pricing/commodities/commodities.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CommoditiesComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var CommoditiesComponent = (function () {
    function CommoditiesComponent() {
    }
    CommoditiesComponent.prototype.ngOnInit = function () {
    };
    return CommoditiesComponent;
}());
CommoditiesComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-commodities',
        template: __webpack_require__("../../../../../src/app/pricing/commodities/commodities.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/commodities/commodities.component.css")]
    }),
    __metadata("design:paramtypes", [])
], CommoditiesComponent);

//# sourceMappingURL=commodities.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.html":
/***/ (function(module, exports) {

module.exports = "<p-dropdown [options]=\"currencies\" [(ngModel)]=\"selectedCurrency\" (onChange)=\"currencyChange()\"></p-dropdown>\n<p-chart type=\"line\" [data]=\"data\"></p-chart>"

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_component__ = __webpack_require__("../../../../../src/app/pricing/pricing.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CurrenciesChartComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var CurrenciesChartComponent = (function () {
    function CurrenciesChartComponent(pricingService, route) {
        this.pricingService = pricingService;
        this.route = route;
        this.unfilteredDataSets = new Array();
        this.currencies = new Array();
        var root = route.snapshot.root;
        var pricingRoute = root.children.find(function (e) { return e.paramMap.keys.indexOf("version") > -1; });
        if (pricingRoute != undefined && pricingRoute.params.version != "latest") {
            this.version = pricingRoute.params.version;
        }
        else {
            // leave undefined
        }
        this.data = {
            labels: [],
            datasets: []
        };
    }
    CurrenciesChartComponent.prototype.ngOnInit = function () {
        this.colorStack = __WEBPACK_IMPORTED_MODULE_1__pricing_component__["a" /* PricingComponent */].COLORS.slice();
        this.loadCurves();
    };
    CurrenciesChartComponent.prototype.loadCurves = function () {
        var _this = this;
        this.pricingService.getCurrencies(this.version)
            .then(function (result) {
            _this.data.labels = result.curves[0].curve.map(function (e) { return e.date; });
            result.curves.forEach(function (element) {
                // TODO: expression curves are ignored for now. Need to be handled on the server
                if (element['@class'] === ".ExpressionCurve") {
                    return;
                }
                _this.unfilteredDataSets.push({
                    currency: element.currency,
                    label: element.name,
                    data: element.curve.map(function (e) { return e.value; }),
                    fill: false,
                    borderColor: _this.colorStack.pop()
                });
            });
            _this.getCurrencies(result.curves);
        });
    };
    CurrenciesChartComponent.prototype.currencyChange = function () {
        this.filterCurrencies(this.selectedCurrency);
    };
    CurrenciesChartComponent.prototype.getCurrencies = function (curves) {
        var currencies = new Set();
        curves.forEach(function (element) {
            currencies.add(element.currency);
        });
        var currencyList = Array.from(currencies);
        for (var i = 0; i < currencyList.length; i++) {
            if (i == 0) {
                this.selectedCurrency = currencyList[i];
                this.filterCurrencies(currencyList[i]);
            }
            this.currencies.push({ label: currencyList[i], value: currencyList[i] });
        }
    };
    CurrenciesChartComponent.prototype.filterCurrencies = function (currency) {
        this.data = {
            datasets: this.unfilteredDataSets.filter(function (e) { return e.currency === currency; }),
            labels: this.data.labels
        };
    };
    return CurrenciesChartComponent;
}());
CurrenciesChartComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-currencies-chart',
        template: __webpack_require__("../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/currencies/currencies-chart/currencies-chart.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__pricing_service__["a" /* PricingService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["ActivatedRoute"]) === "function" && _b || Object])
], CurrenciesChartComponent);

var _a, _b;
//# sourceMappingURL=currencies-chart.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.html":
/***/ (function(module, exports) {

module.exports = "<curve-editor [curves]=\"curves\" (afterChange)=\"afterChange($event)\"></curve-editor>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CurrenciesEditorComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CurrenciesEditorComponent = (function () {
    function CurrenciesEditorComponent(pricingService) {
        this.pricingService = pricingService;
    }
    CurrenciesEditorComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.pricingService.getCurrencies().then(function (currencies) {
            _this.curves = currencies.curves;
        });
    };
    CurrenciesEditorComponent.prototype.afterChange = function (changes) {
        console.log("afterChange called!");
    };
    return CurrenciesEditorComponent;
}());
CurrenciesEditorComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-currencies-editor',
        template: __webpack_require__("../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/currencies/currencies-editor/currencies-editor.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]) === "function" && _a || Object])
], CurrenciesEditorComponent);

var _a;
//# sourceMappingURL=currencies-editor.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies.component.html":
/***/ (function(module, exports) {

module.exports = "<button pButton type=\"button\" [routerLink]=\"['chart']\" icon=\"fa-line-chart\"  class=\"ui-button-secondary\"></button>\n<button pButton type=\"button\" [routerLink]=\"['editor']\" icon=\"fa-table\"  class=\"ui-button-secondary\"></button>\n<router-outlet></router-outlet>"

/***/ }),

/***/ "../../../../../src/app/pricing/currencies/currencies.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__pricing_service__ = __webpack_require__("../../../../../src/app/pricing.service.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CurrenciesComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var CurrenciesComponent = (function () {
    function CurrenciesComponent() {
    }
    CurrenciesComponent.prototype.ngOnInit = function () {
    };
    return CurrenciesComponent;
}());
CurrenciesComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-currencies',
        template: __webpack_require__("../../../../../src/app/pricing/currencies/currencies.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/currencies/currencies.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__pricing_service__["a" /* PricingService */]]
    }),
    __metadata("design:paramtypes", [])
], CurrenciesComponent);

//# sourceMappingURL=currencies.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/curve-editor/curve-editor.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/curve-editor/curve-editor.component.html":
/***/ (function(module, exports) {

module.exports = "<h2>Data Curves</h2>\n<hotTable [data]=\"data\"\n(after-change)=\"afterChange($event)\"\n(after-on-cell-mouse-down)=\"afterOnCellMouseDown($event)\"\n[colHeaders]=\"colHeaders\"\n[options]=\"options\"\n[colWidths]=\"colWidths\">\n</hotTable>\n\n<h2 *ngIf=\"expressionCurves.length > 0\">Expression Curves</h2>\n<div *ngFor=\"let curve of expressionCurves\">\n    <h3>{{curve.name}}</h3>\n    <input type=\"text\" pInputText [(ngModel)]=\"curve.expression\"/>\n</div>\n"

/***/ }),

/***/ "../../../../../src/app/pricing/curve-editor/curve-editor.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return CurveEditorComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var CurveEditorComponent = (function () {
    function CurveEditorComponent() {
        var _this = this;
        /**
         * Only called if there is a change. Null events are swallowed.
         */
        this.afterChange = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
        this.data = new Array();
        this.colHeaders = new Array();
        this.expressionCurves = new Array();
        this.options = {
            // height: 400,
            rowHeaders: new Array(),
            rowHeaderWidth: 100,
            stretchH: 'all',
            columnSorting: true,
            contextMenu: false,
            className: 'htCenter htMiddle',
            afterChange: function (changes) {
                if (changes == null) {
                    return;
                }
                console.log(changes);
                changes.forEach(function (element) {
                    _this.handleAfterChange(element);
                });
            }
        };
    }
    Object.defineProperty(CurveEditorComponent.prototype, "curves", {
        get: function () {
            return this._curves;
        },
        set: function (curves) {
            if (curves === undefined)
                return;
            this.createData(curves);
        },
        enumerable: true,
        configurable: true
    });
    CurveEditorComponent.prototype.handleAfterChange = function (changes) {
        var update = {
            curveName: this.options.rowHeaders[changes[0]],
            curveDate: this.colHeaders[changes[1]],
            oldValue: changes[2],
            newValue: Number(changes[3])
        };
        console.log(update);
        this.afterChange.emit(update);
    };
    CurveEditorComponent.prototype.publishUpdates = function () {
    };
    CurveEditorComponent.prototype.ngOnInit = function () {
    };
    CurveEditorComponent.prototype.createData = function (curves) {
        var _this = this;
        // create headers based on first curve
        // TODO: check for missmatch
        curves.find(function (e) { return e['@name'] === 'DataCurve'; }).curve.forEach(function (e) {
            _this.colHeaders.push(e.date);
        });
        var data = new Array();
        curves.forEach(function (curve) {
            _this.options.rowHeaders.push(curve.name);
            if (curve['@name'] === "ExpressionCurve")
                return;
            data.push(curve.curve.map(function (e) { return e.value; }));
        });
        this.data = data;
        this.expressionCurves = curves.filter(function (e) { return e['@name'] === "ExpressionCurve"; });
    };
    return CurveEditorComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", Object),
    __metadata("design:paramtypes", [Object])
], CurveEditorComponent.prototype, "curves", null);
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Output"])(),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]) === "function" && _a || Object)
], CurveEditorComponent.prototype, "afterChange", void 0);
CurveEditorComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'curve-editor',
        template: __webpack_require__("../../../../../src/app/pricing/curve-editor/curve-editor.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/curve-editor/curve-editor.component.css")]
    }),
    __metadata("design:paramtypes", [])
], CurveEditorComponent);

var _a;
//# sourceMappingURL=curve-editor.component.js.map

/***/ }),

/***/ "../../../../../src/app/pricing/pricing.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".ui-widget-header { \t\r\n\tborder-top-width: 0px !important;\r\n\tborder-left-width: 0px !important;\r\n\tborder-right-width: 0px !important;\r\n\tmargin-top: 12px !important;\r\n\tmargin-bottom: 12px !important;\r\n}\r\n\r\n.ui-widget span.ui-menuitem-text { \t\r\n  font-size: 75%;\r\n}\r\n\r\n.handsontable td, th {\r\n  padding-bottom: 0px !important;\r\n  color: #555555 !important;\r\n  font-family: Arial, Helvetica, sans-serif; \r\n/** font-size: 0.7em; */\r\n  font-size: 12px !important;\r\n}\r\n\r\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/pricing/pricing.component.html":
/***/ (function(module, exports) {

module.exports = "<p-tabMenu [model]=\"items\"></p-tabMenu>\n<!-- <nav>\n    <a [routerLink]=\"['commodities']\" routerLinkActive=\"active\">Commodities</a>\n    <a [routerLink]=\"['currencies']\" routerLinkActive=\"active\">Currencies</a>\n    <a [routerLink]=\"['basefuel']\" routerLinkActive=\"active\">Base Fuel</a>\n    <a [routerLink]=\"['charter']\" routerLinkActive=\"active\">Charter</a>\n</nav> -->\n<router-outlet></router-outlet>"

/***/ }),

/***/ "../../../../../src/app/pricing/pricing.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PricingComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var PricingComponent = (function () {
    function PricingComponent(route) {
        this.route = route;
        // if (route.snapshot.paramMap.has("version")){
        //   this.version = route.snapshot.paramMap.get("version");
        // }
    }
    PricingComponent.prototype.ngOnInit = function () {
        this.items = [
            { label: 'Commodities', icon: 'fa-fire', routerLink: ['commodities'] },
            { label: 'Currencies', icon: 'fa-usd', routerLink: ['currencies'] },
            { label: 'Base Fuel', icon: 'fa-tint', routerLink: ['basefuel'] },
            { label: 'Charter', icon: 'fa-ship', routerLink: ['charter'] }
        ];
    };
    return PricingComponent;
}());
PricingComponent.COLORS = ["#5cbae6", "#b6d957", "#fac364", "#8cd3ff", "#d998cb", "#f2d249", "#93b9c6", "#ccc5a8", "#52bacc", "#dbdb46", "#98aafb"];
PricingComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-pricing',
        template: __webpack_require__("../../../../../src/app/pricing/pricing.component.html"),
        styles: [__webpack_require__("../../../../../src/app/pricing/pricing.component.css")],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None,
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_router__["ActivatedRoute"]) === "function" && _a || Object])
], PricingComponent);

var _a;
//# sourceMappingURL=pricing.component.js.map

/***/ }),

/***/ "../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.html":
/***/ (function(module, exports) {

module.exports = "<section>\n  <p-fieldset legend=\"{{title}}\">\n\n  <h3>In port</h3>\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">In Port Base Rate</span>\n      <p-spinner [step]=\"0.1\" name=\"inPortBaseRateBallast\" [(ngModel)]=\"vesselStateAttributes.inPortBaseRate\"></p-spinner>\n    </div>\n  </div>\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">In Port NBO Rate</span>\n      <p-spinner [step]=\"0.1\" name=\"inPortNBORateBallast\" [(ngModel)]=\"vesselStateAttributes.inPortNBORate\"></p-spinner>\n    </div>\n  </div>\n\n  <h3>Voyage</h3>\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">NBO Rate</span>\n      <p-spinner [step]=\"0.1\" name=\"nboRate\" [(ngModel)]=\"vesselStateAttributes.nboRate\"></p-spinner>\n    </div>\n  </div>\n\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">Idle Base Rate</span>\n      <p-spinner [step]=\"0.1\" name=\"idleBaseRateBallast\" [(ngModel)]=\"vesselStateAttributes.idleBaseRate\"></p-spinner>\n    </div>\n  </div>\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">Idle NBO Rate</span>\n      <p-spinner [step]=\"0.1\" name=\"warmingTimeBallast\" [(ngModel)]=\"vesselStateAttributes.idleNBORate\"></p-spinner>\n    </div>\n  </div>\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-6\">\n      <span class=\"ui-inputgroup-addon\">Service Speed</span>\n      <p-spinner [step]=\"0.1\" name=\"serviceSpedBallast\" [(ngModel)]=\"vesselStateAttributes.serviceSped\"></p-spinner>\n    </div>\n  </div>\n\n  <h3>Fuel Consumption</h3>\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-12\">\n      <p-chart type=\"line\" [data]=\"data\"></p-chart>\n    </div>\n  </div>\n</p-fieldset>\n</section>\n"

/***/ }),

/***/ "../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__ = __webpack_require__("../../../../../generated/vessels/index.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return VesselStateAttributesComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var VesselStateAttributesComponent = (function () {
    function VesselStateAttributesComponent() {
    }
    VesselStateAttributesComponent.prototype.ngOnInit = function () {
    };
    return VesselStateAttributesComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselStateAttributes"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselStateAttributes"]) === "function" && _a || Object)
], VesselStateAttributesComponent.prototype, "vesselStateAttributes", void 0);
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Input"])(),
    __metadata("design:type", String)
], VesselStateAttributesComponent.prototype, "title", void 0);
VesselStateAttributesComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'vessel-state-attributes',
        template: __webpack_require__("../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.html"),
        styles: [__webpack_require__("../../../../../src/app/vessel/vessel-state-attributes/vessel-state-attributes.component.css")]
    }),
    __metadata("design:paramtypes", [])
], VesselStateAttributesComponent);

var _a;
//# sourceMappingURL=vessel-state-attributes.component.js.map

/***/ }),

/***/ "../../../../../src/app/vessel/vessel.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/vessel/vessel.component.html":
/***/ (function(module, exports) {

module.exports = "<h2>{{vessel.name != '' ? vessel.name : ' Vessel'}}</h2>\n\n<form #form=\"ngForm\" (ngSubmit)=\"logForm(form.value)\">\n  <h2>Vessel Attributes</h2>\n  <h3>Route</h3>\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-md-4\">\n      <h3>Speed</h3>\n      <span class=\"ui-inputgroup-addon\">Min Speed</span>\n      <p-spinner name=\"minSpeed\" [(ngModel)]=\"vessel.minSpeed\"></p-spinner>\n\n      <span class=\"ui-inputgroup-addon\">Max Speed</span>\n      <p-spinner name=\"maxSpeed\" [(ngModel)]=\"vessel.maxSpeed\"></p-spinner>\n    </div>\n    \n    <div class=\"ui-g-12 ui-md-4\">\n      <h3>Tanks & heel</h3>\n      <span class=\"ui-inputgroup-addon\">Capacity</span>\n      <p-spinner name=\"capacity\" [(ngModel)]=\"vessel.capacity\"></p-spinner>\n\n      <span class=\"ui-inputgroup-addon\">Cooling Volume</span>\n      <p-spinner name=\"coolingVolume\" [(ngModel)]=\"vessel.coolingVolume\"> </p-spinner>\n\n      <span class=\"ui-inputgroup-addon\">Fill Capacity</span>\n      <p-spinner name=\"fillCapacity\" [(ngModel)]=\"vessel.fillCapacity\"></p-spinner>\n\n      <span class=\"ui-inputgroup-addon\">Min Heel</span>\n      <p-spinner name=\"minHeel\" [(ngModel)]=\"vessel.minHeel\"></p-spinner>\n      \n      <div class=\"ui-inputgroup\">\n        <span class=\"ui-inputgroup-addon\">Has Reliq Capacity</span>\n        <input [(ngModel)]=\"vessel.hasReliqCapacity\" name=\"vessel.hasReliqCapacity\" type=\"checkbox\"/>\n      </div>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <div class=\"ui-inputgroup\">\n        <span class=\"ui-inputgroup-addon\">Base Fuel:</span>\n        <input pInputText type=\"text\"  name=\"baseFuel\" [(ngModel)]=\"vessel.baseFuel\">\n      </div>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <div class=\"ui-inputgroup\">\n        <span class=\"ui-inputgroup-addon\">Imo:</span>\n        <input type=\"text\" name=\"imo\" [(ngModel)]=\"vessel.imo\">\n      </div>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <div class=\"ui-inputgroup\">\n        <span class=\"ui-inputgroup-addon\">Name:</span>\n        <input type=\"text\" name=\"name\" [(ngModel)]=\"vessel.name\">\n      </div>\n    </div>\n\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <span class=\"ui-inputgroup-addon\">Min Base Fuel Consumption</span>\n      <p-spinner name=\"minBaseFuelConsumption\" [(ngModel)]=\"vessel.minBaseFuelConsumption\"></p-spinner>\n    </div>\n\n\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <span class=\"ui-inputgroup-addon\">Pilot Light Rate</span>\n      <p-spinner name=\"pilotLightRate\" [(ngModel)]=\"vessel.pilotLightRate\"></p-spinner>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <span class=\"ui-inputgroup-addon\">Scnt</span>\n      <p-spinner size=\"30\" name=\"scnt\" [(ngModel)]=\"vessel.scnt\"></p-spinner>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n      <span class=\"ui-inputgroup-addon\">Warming Time</span>\n      <p-spinner name=\"warmingTime\" [(ngModel)]=\"vessel.warmingTime\"></p-spinner>\n    </div>\n\n    <div class=\"ui-g-12 ui-md-4\">\n\n    </div>\n  </div>\n\n  <div class=\"ui-g ui-fluid\">\n    <div class=\"ui-g-12 ui-lg-6\">\n      <vessel-state-attributes [vesselStateAttributes]=\"vessel.ladenAttributes\" [title]=\"'Laden Attributes'\"></vessel-state-attributes>\n    </div>\n    <div class=\"ui-g-12 ui-lg-6\">\n      <vessel-state-attributes [vesselStateAttributes]=\"vessel.ballastAttributes\" [title]=\"'Ballast Attributes'\"></vessel-state-attributes>\n    </div>\n    </div>\n  <button pButton type=\"submit\" label=\"Submit\"></button>\n</form>\n"

/***/ }),

/***/ "../../../../../src/app/vessel/vessel.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__ = __webpack_require__("../../../../../generated/vessels/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return VesselComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};



var VesselComponent = (function () {
    function VesselComponent(activatedRoute, vesselsService) {
        this.activatedRoute = activatedRoute;
        this.vesselsService = vesselsService;
        this.version = {
            identifier: '',
            vessels: null
        };
        this.fuelConsumption = [{
                consumption: 0,
                speed: 0
            }];
        this.ladenAttributes = {
            fuelConsumption: this.fuelConsumption,
            idleBaseRate: 0,
            idleNBORate: 0,
            inPortBaseRate: 0,
            inPortNBORate: 0,
            nboRate: 0,
            serviceSped: 0
        };
        this.ballastAttributes = {
            fuelConsumption: this.fuelConsumption,
            idleBaseRate: 0,
            idleNBORate: 0,
            inPortBaseRate: 0,
            inPortNBORate: 0,
            nboRate: 0,
            serviceSped: 0
        };
        this.vessel = {
            ballastAttributes: this.ballastAttributes,
            ladenAttributes: this.ladenAttributes,
            baseFuel: 'HFO',
            capacity: 0,
            coolingVolume: 0,
            fillCapacity: 0,
            hasReliqCapacity: false,
            imo: '1',
            inaccessiblePorts: new Array(),
            inaccessibleRoutes: new Array(),
            maxSpeed: 0,
            minBaseFuelConsumption: 0,
            minHeel: 0,
            minSpeed: 0,
            mmxId: '',
            name: '',
            pilotLightRate: 0,
            scnt: 0,
            warmingTime: 0
        };
    }
    VesselComponent.prototype.ngOnInit = function () {
        var _this = this;
        // subscribe to router event
        this.activatedRoute.params.subscribe(function (params) {
            var vesselId = params['version'];
            if (vesselId) {
                _this.getVessel(vesselId);
            }
        });
    };
    VesselComponent.prototype.getVessel = function (mmxId) {
        var _this = this;
        this.vesselsService.getVesselUsingGET(mmxId).toPromise()
            .then(function (vessel) { return _this.vessel = vessel; })
            .catch(function (error) { return console.log(error); });
    };
    VesselComponent.prototype.logForm = function () {
        this.vesselsService.insertVesselUsingPOST(this.vessel).toPromise()
            .then(function (vessel) { return vessel; })
            .catch(function (error) { return console.log(error); });
    };
    return VesselComponent;
}());
VesselComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-vessel',
        template: __webpack_require__("../../../../../src/app/vessel/vessel.component.html"),
        styles: [__webpack_require__("../../../../../src/app/vessel/vessel.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__angular_router__["ActivatedRoute"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_router__["ActivatedRoute"]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"]) === "function" && _b || Object])
], VesselComponent);

var _a, _b;
//# sourceMappingURL=vessel.component.js.map

/***/ }),

/***/ "../../../../../src/app/vessels/customCells/customCells.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "p-multiSelect {\n  width: 100%;\n}\n\nmy-table-cell {\n  white-space: normal;\n  overflow-y: visible !important;\n  overflow-x: visible !important;\n}\n\nmy-table-cell-test {\n  white-space: normal;\n  overflow-y: visible !important;\n  overflow-x: visible !important;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/vessels/customCells/customCells.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "e", function() { return FuelCellRendererService; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "c", function() { return RouteCellRendererService; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "d", function() { return PortCellRendererService; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return MyTableCellComponent; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return MyTableCellComponentTest; });
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var FuelCellRendererService = (function () {
    function FuelCellRendererService() {
        this.fuelCellRendererEvent = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
    }
    FuelCellRendererService.prototype.update = function (data) {
        this.fuelCellRendererEvent.emit(data);
    };
    return FuelCellRendererService;
}());
FuelCellRendererService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
], FuelCellRendererService);

var RouteCellRendererService = (function () {
    function RouteCellRendererService() {
        this.routeCellRendererEvent = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
    }
    RouteCellRendererService.prototype.update = function (data) {
        this.routeCellRendererEvent.emit(data);
    };
    return RouteCellRendererService;
}());
RouteCellRendererService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
], RouteCellRendererService);

var PortCellRendererService = (function () {
    function PortCellRendererService() {
        this.portCellRendererEvent = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["EventEmitter"]();
    }
    PortCellRendererService.prototype.update = function (data) {
        this.portCellRendererEvent.emit(data);
    };
    return PortCellRendererService;
}());
PortCellRendererService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])()
], PortCellRendererService);

var MyTableCellComponent = (function () {
    function MyTableCellComponent(routeCellRendererService, portCellRendererService) {
        this.routeCellRendererService = routeCellRendererService;
        this.portCellRendererService = portCellRendererService;
        this.selected = [];
    }
    MyTableCellComponent.prototype.setSelection = function (data) {
        this.selectionData = data.map(function (x) { return ({ label: x, value: x }); });
    };
    MyTableCellComponent.prototype.change = function (event) {
        if (this.componentType == 'route') {
            this.routeCellRendererService.update({ index: this.index, data: event });
        }
        else if (this.componentType == 'port') {
            this.portCellRendererService.update({ index: this.index, data: event });
        }
    };
    return MyTableCellComponent;
}());
MyTableCellComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'my-table-cell',
        template: __webpack_require__("../../../../../src/app/vessels/customCells/templates/myTableCell.component.html"),
        styles: [__webpack_require__("../../../../../src/app/vessels/customCells/customCells.component.css")],
        providers: [],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None,
    }),
    __metadata("design:paramtypes", [RouteCellRendererService, PortCellRendererService])
], MyTableCellComponent);

var MyTableCellComponentTest = (function (_super) {
    __extends(MyTableCellComponentTest, _super);
    function MyTableCellComponentTest(routeCellRendererService, portCellRendererService, fuelCellRendererService) {
        var _this = _super.call(this, routeCellRendererService, portCellRendererService) || this;
        _this.routeCellRendererService = routeCellRendererService;
        _this.portCellRendererService = portCellRendererService;
        _this.fuelCellRendererService = fuelCellRendererService;
        _this.showTable = false;
        _this.options = {};
        _this.chartOptions = { 'legend': { 'display': false } };
        return _this;
    }
    MyTableCellComponentTest.prototype.setSelection = function (data) {
        this.data = data.map(function (x) { return ([x.speed, x.consumption]); });
        this.chartData = this.generateChartData();
        this.options = {
            data: this.data,
            manualRowMove: true,
            columns: [{ title: 'Speed', type: 'numeric', allowInvalid: false }, { title: 'Consumption', type: 'numeric' }],
        };
    };
    MyTableCellComponentTest.prototype.ngOnInit = function () {
        this.options = {
            data: this.data,
            manualRowMove: true,
            columns: [{ title: 'Speed', type: 'numeric', allowInvalid: false }, { title: 'Consumption', type: 'numeric' }],
        };
    };
    MyTableCellComponentTest.prototype.toggleTable = function () {
        this.showTable = !this.showTable;
    };
    MyTableCellComponentTest.prototype.change = function (event) {
        this.chartData = this.generateChartData();
        this.fuelCellRendererService.update({ index: this.index, dest: this.dest, data: this.data });
    };
    MyTableCellComponentTest.prototype.generateChartData = function () {
        return { 'labels': this.data.map(function (x) { return x[0]; }),
            'datasets': [
                {
                    'label': 'Fuel cost',
                    'data': this.data.map(function (x) { return x[1]; }),
                    'fill': false,
                    'borderColor': '#4bc0c0'
                }
            ],
        };
    };
    return MyTableCellComponentTest;
}(MyTableCellComponent));
MyTableCellComponentTest = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'my-table-cell-test',
        template: __webpack_require__("../../../../../src/app/vessels/customCells/templates/myTableCellTest.component.html"),
        styles: [__webpack_require__("../../../../../src/app/vessels/customCells/customCells.component.css")],
        providers: [],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None,
    }),
    __metadata("design:paramtypes", [RouteCellRendererService,
        PortCellRendererService,
        FuelCellRendererService])
], MyTableCellComponentTest);

//# sourceMappingURL=customCells.component.js.map

/***/ }),

/***/ "../../../../../src/app/vessels/customCells/templates/myTableCell.component.html":
/***/ (function(module, exports) {

module.exports = "<p-multiSelect (onChange)=\"change($event.value)\" [style]=\"{'width':'100%'}\" [options]=\"selectionData\" [(ngModel)]=\"selected\" optionLabel=\"name\"></p-multiSelect>\n"

/***/ }),

/***/ "../../../../../src/app/vessels/customCells/templates/myTableCellTest.component.html":
/***/ (function(module, exports) {

module.exports = "<button pButton (click)=\"toggleTable()\" class=\"ui-button-secondary\" icon=\"fa-edit\" type=\"submit\"></button>\n <hotTable *ngIf=\"showTable\"\n  (afterChange)=\"change($event)\"\n  [options]=\"options\">\n </hotTable>\n<p-chart type=\"line\" [data]=\"chartData\" [options]=\"chartOptions\"></p-chart>\n"

/***/ }),

/***/ "../../../../../src/app/vessels/vessels.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".handsontable td {\n  white-space: normal;\n  overflow:visible !important;\n}\n\n.handsontable td, th {\n  padding-bottom: 0px !important;\n  color: #555555 !important;\n  font-family: Arial, Helvetica, sans-serif; \n/** font-size: 0.7em; */\n  font-size: 12px !important;\n}\n\n.handsontable th {\n  background-color: #e0f0ff !important; \n}\n\nbody{\n  margin: 1;\n}\n\nbutton {\n  font-family: Cambria, Georgia;\n  background-color: #eee;\n  border: none;\n  border-width: 0px !important;\n  padding: 5px 10px;\n  margin-bottom: 6px;\n  border-radius: 4px;\n  cursor: pointer;\n  cursor: hand;\n}\n\nbutton:hover {\n  background-color: #cfd8dc;\n}\n\nbutton.delete {\n  float:right;\n  margin-top: 2px;\n  margin-right: .8em;\n  background-color: gray !important;\n  color:white;\n}", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/vessels/vessels.component.html":
/***/ (function(module, exports) {

module.exports = "<br />\n<button pButton (click)=\"saveVessels()\" class=\"ui-button-secondary\" icon=\"fa-save\" type=\"submit\"></button>\n\n<hotTable \n[options]=\"options\">\n</hotTable>\n\n<div #templateContainer\n  [hidden]=\"true\">\n</div>\n"

/***/ }),

/***/ "../../../../../src/app/vessels/vessels.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__ = __webpack_require__("../../../../../generated/vessels/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_ng2_handsontable__ = __webpack_require__("../../../../ng2-handsontable/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_handsontable__ = __webpack_require__("../../../../handsontable/dist/handsontable.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_handsontable___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_handsontable__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_jquery__ = __webpack_require__("../../../../jquery/dist/jquery.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_jquery___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_jquery__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__ = __webpack_require__("../../../../../src/app/vessels/customCells/customCells.component.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return VesselsComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};








var VesselsComponent = (function () {
    function VesselsComponent(vesselsService, factoryResolver, routeCellRendererService, portCellRendererService, fuelCellRendererService) {
        var _this = this;
        this.vesselsService = vesselsService;
        this.factoryResolver = factoryResolver;
        this.routeCellRendererService = routeCellRendererService;
        this.portCellRendererService = portCellRendererService;
        this.fuelCellRendererService = fuelCellRendererService;
        this.ignoreKeyList = ['mmxId', 'ladenAttributes', 'ballastAttributes'];
        this.routes = ['Panama canal', 'Suez canal'];
        this.ports = ['PortA', 'PortB'];
        this.cellPortTemplateComponents = {};
        this.cellRouteTemplateComponents = {};
        this.cellTestTemplateComponents = {};
        this.cellTest2TemplateComponents = {};
        this.columns = [
            { data: 'name', title: 'Name', },
            { data: "imo", title: 'IMO' },
            { data: "inaccessiblePorts", title: 'Disallowed Ports', renderer: this.rendererInaccessiblePortLinkCell.bind(this), readOnly: true, },
            { data: "inaccessibleRoutes", title: 'Disallowed Canals', renderer: this.rendererInacessibleRouteLinkCell.bind(this), readOnly: true, },
            { data: "baseFuel", title: 'Base Fuel' },
            { data: "capacity", title: 'Capacity' },
            { data: "fillCapacity", title: 'Fill Capacity' },
            { data: "minSpeed", title: 'Min Speed' },
            { data: "maxSpeed", title: 'Max Speed' },
            { data: "minHeel", title: 'Min Heel' },
            { data: "warmingTime", title: 'Warming Time' },
            { data: "coolingVolume", title: 'Cooling Volume' },
            { data: "pilotLightRate", title: 'Pilot Light Rate' },
            { data: "minBaseFuelConsumption", title: 'Min Base Fuel Consumption' },
            { data: "hasReliqCapacity", title: 'Reliq', type: 'checkbox' },
            { data: "scnt", title: 'SCNT' },
            { data: "nboRate", title: 'NBO Rate' },
            { data: "ladenAttributes.fuelConsumption", title: 'Laden Fuel Curve', renderer: this.rendererFuelConsumptionLinkCell.bind(this), readOnly: true, editor: __WEBPACK_IMPORTED_MODULE_3_handsontable__["editors"].TextEditor },
            { data: "ladenAttributes.idleNBORate", title: 'Laden Idle NBO Rate', },
            { data: "ladenAttributes.idleBaseRate", title: 'Laden Idle Base Rate' },
            { data: "ladenAttributes.inPortBaseRate", title: 'Laden In Port Base Rate' },
            { data: "ladenAttributes.serviceSped", title: 'Laden Service Speed ' },
            { data: "ladenAttributes.inPortNBORate", title: 'Laden In Port NBO Rate' },
            { data: "ballastAttributes.fuelConsumption", title: 'Ballast Fuel Curve', renderer: this.rendererFuelConsumption2LinkCell.bind(this), readOnly: true, editor: __WEBPACK_IMPORTED_MODULE_3_handsontable__["editors"].TextEditor },
            { data: "ballastAttributes.idleNBORate", title: 'Ballast Idle NBO Rate' },
            { data: "ballastAttributes.idleBaseRate", title: 'Ballast Idle Base Rate' },
            { data: "ballastAttributes.inPortBaseRate", title: 'Ballast In Port Base Rate' },
            { data: "ballastAttributes.serviceSped", title: 'Ballast Service Speed' },
            { data: "ballastAttributes.inPortNBORate", title: 'Ballast In Port NBO Rate' },
        ];
        this.routeCellRendererService.routeCellRendererEvent.subscribe(function (data) {
            _this.vessels[data.index]['inaccessibleRoutes'] = data.data;
        });
        this.portCellRendererService.portCellRendererEvent.subscribe(function (data) {
            _this.vessels[data.index]['inaccessiblePorts'] = data.data;
        });
        this.fuelCellRendererService.fuelCellRendererEvent.subscribe(function (data) {
            if (data.dest === "laden") {
                console.log("updated laden consumption");
                _this.vessels[data.index]['ladenAttributes']['fuelConsumption'] = data.data.map(function (x) { return ({ speed: x[1], consumption: x[0] }); });
            }
            else if (data.dest === "ballast") {
                console.log("updated laden consumption");
                _this.vessels[data.index]['ballastAttributes']['fuelConsumption'] = data.data.map(function (x) { return ({ speed: x[1], consumption: x[0] }); });
            }
        });
        this.getVessels();
    }
    VesselsComponent.prototype.getVessels = function () {
        var _this = this;
        this.vesselsService.getVesselsUsingGET().toPromise()
            .then(function (vessels) {
            _this.vessels = vessels;
            _this.refreshTable();
        })
            .catch(function (error) {
            console.log(error);
            _this.vessels = [];
            _this.refreshTable();
        });
    };
    VesselsComponent.prototype.refreshTable = function () {
        this.createHorizontalTable(this.vessels);
    };
    VesselsComponent.prototype.createEmptyVessel = function () {
        var fuelConsumptionLadenEmpty = [];
        var fuelConsumptionBallastEmpty = [];
        var ladenAttributeEmpty = {
            "fuelConsumption": fuelConsumptionLadenEmpty,
            "idleBaseRate": null,
            "idleNBORate": null,
            "inPortBaseRate": null,
            "inPortNBORate": null,
            "nboRate": null,
            "serviceSped": null
        };
        var ballastAttributeEmpty = {
            "fuelConsumption": fuelConsumptionBallastEmpty,
            "idleBaseRate": null,
            "idleNBORate": null,
            "inPortBaseRate": null,
            "inPortNBORate": null,
            "nboRate": null,
            "serviceSped": null
        };
        var emptyVessel = { "mmxId": "",
            "name": "",
            "imo": "",
            "inaccessiblePorts": [],
            "inaccessibleRoutes": [],
            "baseFuel": "",
            "capacity": null,
            "fillCapacity": null,
            "ladenAttributes": ladenAttributeEmpty,
            "ballastAttributes": ballastAttributeEmpty,
            "minSpeed": null,
            "maxSpeed": null,
            "minHeel": null,
            "warmingTime": null,
            "coolingVolume": null,
            "pilotLightRate": null,
            "minBaseFuelConsumption": null,
            "hasReliqCapacity": false,
            "scnt": null };
        return emptyVessel;
    };
    VesselsComponent.prototype.createHorizontalTable = function (vessels) {
        var rowHeaders = [];
        this.colHeaders = [];
        if (vessels && vessels.length === 0) {
            vessels.push(this.createEmptyVessel());
        }
        ;
        rowHeaders = vessels.map(function (v) { return v.mmxId; });
        this.colHeaders = this.columns.map(function (col) { return col['title']; });
        this.options = {
            data: this.vessels,
            rowHeaders: rowHeaders,
            hiddenColumns: true,
            //colHeaders: true,
            //nestedHeaders: [
            //  [{label:'test', colspan:17}, {label:'LadenAttributes', colspan:6}, {label:'Ballast attributes', colspan:6}],
            //  this.colHeaders
            //],
            //collapsibleColumns: [{row: -2, col: 1, collapsible: true}],
            colHeaders: this.colHeaders,
            columns: this.columns,
            rowHeaderWidth: 100,
            autoColumnSize: true,
            manualColumnMove: true,
            manualRowMove: true,
            manualColumnResize: true,
            manualRowResize: true,
            contextMenu: true,
            search: true,
            columnSorting: true,
            sortIndicator: true,
            undo: true,
        };
        this.cellComponentFactory = this.factoryResolver
            .resolveComponentFactory(__WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["a" /* MyTableCellComponent */]);
        this.cellComponentTestFactory = this.factoryResolver
            .resolveComponentFactory(__WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["b" /* MyTableCellComponentTest */]);
    };
    VesselsComponent.prototype.rendererInaccessiblePortLinkCell = function (instance, td, row, col, columnKey, data, cellProperties) {
        var component = this.cellPortTemplateComponents[row];
        if (!component) {
            component = this.templateContainer
                .createComponent(this.cellComponentFactory);
            var vessel = this.vessels[row];
            if (vessel['inaccessiblePorts'] == null || vessel['inaccessiblePorts'] == undefined) {
                vessel['inaccessiblePorts'] = [];
            }
            component.instance.selected = vessel['inaccessiblePorts'];
            Object.assign(component.instance, { index: row, componentType: 'port' });
            component.instance.setSelection(this.ports);
            component.changeDetectorRef.detectChanges();
            this.cellPortTemplateComponents[row] = component;
        }
        __WEBPACK_IMPORTED_MODULE_4_jquery__(td).html(component.location.nativeElement);
    };
    ;
    VesselsComponent.prototype.rendererFuelConsumptionLinkCell = function (instance, td, row, col, columnKey, data, cellProperties) {
        var component = this.cellTestTemplateComponents[row];
        if (!component) {
            component = this.templateContainer
                .createComponent(this.cellComponentTestFactory);
            var vessel = this.vessels[row];
            if (vessel['ladenAttributes'] == null || vessel['ladenAttributes'] == undefined) {
                vessel['ladenAttributes'] = this.createEmptyVessel()['ladenAttributes'];
            }
            Object.assign(component.instance, { index: row, dest: 'laden' });
            component.changeDetectorRef.detectChanges();
            component.instance.setSelection(this.vessels[row]['ladenAttributes']['fuelConsumption']);
            this.cellTestTemplateComponents[row] = component;
        }
        __WEBPACK_IMPORTED_MODULE_4_jquery__(td).html(component.location.nativeElement);
    };
    ;
    VesselsComponent.prototype.rendererFuelConsumption2LinkCell = function (instance, td, row, col, columnKey, data, cellProperties) {
        var component = this.cellTest2TemplateComponents[row];
        if (!component) {
            component = this.templateContainer
                .createComponent(this.cellComponentTestFactory);
            var vessel = this.vessels[row];
            if (vessel['ballastAttributes'] == null || vessel['ballastAttributes'] == undefined) {
                vessel['ballastAttributes'] = this.createEmptyVessel()['ballastAttributes'];
            }
            Object.assign(component.instance, { index: row, dest: 'ballast' });
            component.instance.setSelection(this.vessels[row]['ballastAttributes']['fuelConsumption']);
            component.changeDetectorRef.detectChanges();
            this.cellTest2TemplateComponents[row] = component;
        }
        __WEBPACK_IMPORTED_MODULE_4_jquery__(td).html(component.location.nativeElement);
    };
    ;
    VesselsComponent.prototype.rendererInacessibleRouteLinkCell = function (instance, td, row, col, columnKey, data, cellProperties) {
        var component = this.cellRouteTemplateComponents[row];
        if (!component) {
            component = this.templateContainer
                .createComponent(this.cellComponentFactory);
            var vessel = this.vessels[row];
            if (vessel['inaccessibleRoutes'] == null || vessel['inaccessibleRoutes'] == undefined) {
                vessel['inaccessibleRoutes'] = [];
            }
            component.instance.selected = vessel['inaccessibleRoutes'];
            Object.assign(component.instance, { index: row, componentType: 'route' });
            component.instance.setSelection(this.routes);
            component.changeDetectorRef.detectChanges();
            this.cellRouteTemplateComponents[row] = component;
        }
        __WEBPACK_IMPORTED_MODULE_4_jquery__(td).html(component.location.nativeElement);
    };
    ;
    VesselsComponent.prototype.saveVessels = function () {
        var _this = this;
        this.vessels = this.vessels.map(function (v) {
            return Object.assign(_this.createEmptyVessel(), v);
        });
        this.vesselsService.insertVesselsUsingPOST(this.vessels).toPromise()
            .then(function (vessels) {
            console.log("OK " + vessels);
        })
            .catch(function (error) { return console.log(error); });
    };
    ;
    return VesselsComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])('templateContainer', { read: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"] }),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewContainerRef"]) === "function" && _a || Object)
], VesselsComponent.prototype, "templateContainer", void 0);
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewChild"])(__WEBPACK_IMPORTED_MODULE_2_ng2_handsontable__["b" /* HotTable */]),
    __metadata("design:type", typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_2_ng2_handsontable__["b" /* HotTable */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2_ng2_handsontable__["b" /* HotTable */]) === "function" && _b || Object)
], VesselsComponent.prototype, "hotTable", void 0);
VesselsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'app-vessels',
        template: __webpack_require__("../../../../../src/app/vessels/vessels.component.html"),
        styles: [__webpack_require__("../../../../../src/app/vessels/vessels.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"]],
        encapsulation: __WEBPACK_IMPORTED_MODULE_0__angular_core__["ViewEncapsulation"].None
    }),
    __metadata("design:paramtypes", [typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_vessels_index__["VesselsService"]) === "function" && _c || Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_0__angular_core__["ComponentFactoryResolver"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_0__angular_core__["ComponentFactoryResolver"]) === "function" && _d || Object, typeof (_e = typeof __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["c" /* RouteCellRendererService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["c" /* RouteCellRendererService */]) === "function" && _e || Object, typeof (_f = typeof __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["d" /* PortCellRendererService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["d" /* PortCellRendererService */]) === "function" && _f || Object, typeof (_g = typeof __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["e" /* FuelCellRendererService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__customCells_customCells_component__["e" /* FuelCellRendererService */]) === "function" && _g || Object])
], VesselsComponent);

var _a, _b, _c, _d, _e, _f, _g;
//# sourceMappingURL=vessels.component.js.map

/***/ }),

/***/ "../../../../../src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
// The file contents for the current environment will overwrite these during build.
var environment = {
    production: false
};
//# sourceMappingURL=environment.js.map

/***/ }),

/***/ "../../../../../src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("../../../platform-browser-dynamic/@angular/platform-browser-dynamic.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("../../../../../src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("../../../../../src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["enableProdMode"])();
}
__webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */])
    .catch(function (err) { return console.log(err); });
//# sourceMappingURL=main.js.map

/***/ }),

/***/ "../../../../moment/locale recursive ^\\.\\/.*$":
/***/ (function(module, exports, __webpack_require__) {

var map = {
	"./af": "../../../../moment/locale/af.js",
	"./af.js": "../../../../moment/locale/af.js",
	"./ar": "../../../../moment/locale/ar.js",
	"./ar-dz": "../../../../moment/locale/ar-dz.js",
	"./ar-dz.js": "../../../../moment/locale/ar-dz.js",
	"./ar-kw": "../../../../moment/locale/ar-kw.js",
	"./ar-kw.js": "../../../../moment/locale/ar-kw.js",
	"./ar-ly": "../../../../moment/locale/ar-ly.js",
	"./ar-ly.js": "../../../../moment/locale/ar-ly.js",
	"./ar-ma": "../../../../moment/locale/ar-ma.js",
	"./ar-ma.js": "../../../../moment/locale/ar-ma.js",
	"./ar-sa": "../../../../moment/locale/ar-sa.js",
	"./ar-sa.js": "../../../../moment/locale/ar-sa.js",
	"./ar-tn": "../../../../moment/locale/ar-tn.js",
	"./ar-tn.js": "../../../../moment/locale/ar-tn.js",
	"./ar.js": "../../../../moment/locale/ar.js",
	"./az": "../../../../moment/locale/az.js",
	"./az.js": "../../../../moment/locale/az.js",
	"./be": "../../../../moment/locale/be.js",
	"./be.js": "../../../../moment/locale/be.js",
	"./bg": "../../../../moment/locale/bg.js",
	"./bg.js": "../../../../moment/locale/bg.js",
	"./bn": "../../../../moment/locale/bn.js",
	"./bn.js": "../../../../moment/locale/bn.js",
	"./bo": "../../../../moment/locale/bo.js",
	"./bo.js": "../../../../moment/locale/bo.js",
	"./br": "../../../../moment/locale/br.js",
	"./br.js": "../../../../moment/locale/br.js",
	"./bs": "../../../../moment/locale/bs.js",
	"./bs.js": "../../../../moment/locale/bs.js",
	"./ca": "../../../../moment/locale/ca.js",
	"./ca.js": "../../../../moment/locale/ca.js",
	"./cs": "../../../../moment/locale/cs.js",
	"./cs.js": "../../../../moment/locale/cs.js",
	"./cv": "../../../../moment/locale/cv.js",
	"./cv.js": "../../../../moment/locale/cv.js",
	"./cy": "../../../../moment/locale/cy.js",
	"./cy.js": "../../../../moment/locale/cy.js",
	"./da": "../../../../moment/locale/da.js",
	"./da.js": "../../../../moment/locale/da.js",
	"./de": "../../../../moment/locale/de.js",
	"./de-at": "../../../../moment/locale/de-at.js",
	"./de-at.js": "../../../../moment/locale/de-at.js",
	"./de-ch": "../../../../moment/locale/de-ch.js",
	"./de-ch.js": "../../../../moment/locale/de-ch.js",
	"./de.js": "../../../../moment/locale/de.js",
	"./dv": "../../../../moment/locale/dv.js",
	"./dv.js": "../../../../moment/locale/dv.js",
	"./el": "../../../../moment/locale/el.js",
	"./el.js": "../../../../moment/locale/el.js",
	"./en-au": "../../../../moment/locale/en-au.js",
	"./en-au.js": "../../../../moment/locale/en-au.js",
	"./en-ca": "../../../../moment/locale/en-ca.js",
	"./en-ca.js": "../../../../moment/locale/en-ca.js",
	"./en-gb": "../../../../moment/locale/en-gb.js",
	"./en-gb.js": "../../../../moment/locale/en-gb.js",
	"./en-ie": "../../../../moment/locale/en-ie.js",
	"./en-ie.js": "../../../../moment/locale/en-ie.js",
	"./en-nz": "../../../../moment/locale/en-nz.js",
	"./en-nz.js": "../../../../moment/locale/en-nz.js",
	"./eo": "../../../../moment/locale/eo.js",
	"./eo.js": "../../../../moment/locale/eo.js",
	"./es": "../../../../moment/locale/es.js",
	"./es-do": "../../../../moment/locale/es-do.js",
	"./es-do.js": "../../../../moment/locale/es-do.js",
	"./es.js": "../../../../moment/locale/es.js",
	"./et": "../../../../moment/locale/et.js",
	"./et.js": "../../../../moment/locale/et.js",
	"./eu": "../../../../moment/locale/eu.js",
	"./eu.js": "../../../../moment/locale/eu.js",
	"./fa": "../../../../moment/locale/fa.js",
	"./fa.js": "../../../../moment/locale/fa.js",
	"./fi": "../../../../moment/locale/fi.js",
	"./fi.js": "../../../../moment/locale/fi.js",
	"./fo": "../../../../moment/locale/fo.js",
	"./fo.js": "../../../../moment/locale/fo.js",
	"./fr": "../../../../moment/locale/fr.js",
	"./fr-ca": "../../../../moment/locale/fr-ca.js",
	"./fr-ca.js": "../../../../moment/locale/fr-ca.js",
	"./fr-ch": "../../../../moment/locale/fr-ch.js",
	"./fr-ch.js": "../../../../moment/locale/fr-ch.js",
	"./fr.js": "../../../../moment/locale/fr.js",
	"./fy": "../../../../moment/locale/fy.js",
	"./fy.js": "../../../../moment/locale/fy.js",
	"./gd": "../../../../moment/locale/gd.js",
	"./gd.js": "../../../../moment/locale/gd.js",
	"./gl": "../../../../moment/locale/gl.js",
	"./gl.js": "../../../../moment/locale/gl.js",
	"./gom-latn": "../../../../moment/locale/gom-latn.js",
	"./gom-latn.js": "../../../../moment/locale/gom-latn.js",
	"./he": "../../../../moment/locale/he.js",
	"./he.js": "../../../../moment/locale/he.js",
	"./hi": "../../../../moment/locale/hi.js",
	"./hi.js": "../../../../moment/locale/hi.js",
	"./hr": "../../../../moment/locale/hr.js",
	"./hr.js": "../../../../moment/locale/hr.js",
	"./hu": "../../../../moment/locale/hu.js",
	"./hu.js": "../../../../moment/locale/hu.js",
	"./hy-am": "../../../../moment/locale/hy-am.js",
	"./hy-am.js": "../../../../moment/locale/hy-am.js",
	"./id": "../../../../moment/locale/id.js",
	"./id.js": "../../../../moment/locale/id.js",
	"./is": "../../../../moment/locale/is.js",
	"./is.js": "../../../../moment/locale/is.js",
	"./it": "../../../../moment/locale/it.js",
	"./it.js": "../../../../moment/locale/it.js",
	"./ja": "../../../../moment/locale/ja.js",
	"./ja.js": "../../../../moment/locale/ja.js",
	"./jv": "../../../../moment/locale/jv.js",
	"./jv.js": "../../../../moment/locale/jv.js",
	"./ka": "../../../../moment/locale/ka.js",
	"./ka.js": "../../../../moment/locale/ka.js",
	"./kk": "../../../../moment/locale/kk.js",
	"./kk.js": "../../../../moment/locale/kk.js",
	"./km": "../../../../moment/locale/km.js",
	"./km.js": "../../../../moment/locale/km.js",
	"./kn": "../../../../moment/locale/kn.js",
	"./kn.js": "../../../../moment/locale/kn.js",
	"./ko": "../../../../moment/locale/ko.js",
	"./ko.js": "../../../../moment/locale/ko.js",
	"./ky": "../../../../moment/locale/ky.js",
	"./ky.js": "../../../../moment/locale/ky.js",
	"./lb": "../../../../moment/locale/lb.js",
	"./lb.js": "../../../../moment/locale/lb.js",
	"./lo": "../../../../moment/locale/lo.js",
	"./lo.js": "../../../../moment/locale/lo.js",
	"./lt": "../../../../moment/locale/lt.js",
	"./lt.js": "../../../../moment/locale/lt.js",
	"./lv": "../../../../moment/locale/lv.js",
	"./lv.js": "../../../../moment/locale/lv.js",
	"./me": "../../../../moment/locale/me.js",
	"./me.js": "../../../../moment/locale/me.js",
	"./mi": "../../../../moment/locale/mi.js",
	"./mi.js": "../../../../moment/locale/mi.js",
	"./mk": "../../../../moment/locale/mk.js",
	"./mk.js": "../../../../moment/locale/mk.js",
	"./ml": "../../../../moment/locale/ml.js",
	"./ml.js": "../../../../moment/locale/ml.js",
	"./mr": "../../../../moment/locale/mr.js",
	"./mr.js": "../../../../moment/locale/mr.js",
	"./ms": "../../../../moment/locale/ms.js",
	"./ms-my": "../../../../moment/locale/ms-my.js",
	"./ms-my.js": "../../../../moment/locale/ms-my.js",
	"./ms.js": "../../../../moment/locale/ms.js",
	"./my": "../../../../moment/locale/my.js",
	"./my.js": "../../../../moment/locale/my.js",
	"./nb": "../../../../moment/locale/nb.js",
	"./nb.js": "../../../../moment/locale/nb.js",
	"./ne": "../../../../moment/locale/ne.js",
	"./ne.js": "../../../../moment/locale/ne.js",
	"./nl": "../../../../moment/locale/nl.js",
	"./nl-be": "../../../../moment/locale/nl-be.js",
	"./nl-be.js": "../../../../moment/locale/nl-be.js",
	"./nl.js": "../../../../moment/locale/nl.js",
	"./nn": "../../../../moment/locale/nn.js",
	"./nn.js": "../../../../moment/locale/nn.js",
	"./pa-in": "../../../../moment/locale/pa-in.js",
	"./pa-in.js": "../../../../moment/locale/pa-in.js",
	"./pl": "../../../../moment/locale/pl.js",
	"./pl.js": "../../../../moment/locale/pl.js",
	"./pt": "../../../../moment/locale/pt.js",
	"./pt-br": "../../../../moment/locale/pt-br.js",
	"./pt-br.js": "../../../../moment/locale/pt-br.js",
	"./pt.js": "../../../../moment/locale/pt.js",
	"./ro": "../../../../moment/locale/ro.js",
	"./ro.js": "../../../../moment/locale/ro.js",
	"./ru": "../../../../moment/locale/ru.js",
	"./ru.js": "../../../../moment/locale/ru.js",
	"./sd": "../../../../moment/locale/sd.js",
	"./sd.js": "../../../../moment/locale/sd.js",
	"./se": "../../../../moment/locale/se.js",
	"./se.js": "../../../../moment/locale/se.js",
	"./si": "../../../../moment/locale/si.js",
	"./si.js": "../../../../moment/locale/si.js",
	"./sk": "../../../../moment/locale/sk.js",
	"./sk.js": "../../../../moment/locale/sk.js",
	"./sl": "../../../../moment/locale/sl.js",
	"./sl.js": "../../../../moment/locale/sl.js",
	"./sq": "../../../../moment/locale/sq.js",
	"./sq.js": "../../../../moment/locale/sq.js",
	"./sr": "../../../../moment/locale/sr.js",
	"./sr-cyrl": "../../../../moment/locale/sr-cyrl.js",
	"./sr-cyrl.js": "../../../../moment/locale/sr-cyrl.js",
	"./sr.js": "../../../../moment/locale/sr.js",
	"./ss": "../../../../moment/locale/ss.js",
	"./ss.js": "../../../../moment/locale/ss.js",
	"./sv": "../../../../moment/locale/sv.js",
	"./sv.js": "../../../../moment/locale/sv.js",
	"./sw": "../../../../moment/locale/sw.js",
	"./sw.js": "../../../../moment/locale/sw.js",
	"./ta": "../../../../moment/locale/ta.js",
	"./ta.js": "../../../../moment/locale/ta.js",
	"./te": "../../../../moment/locale/te.js",
	"./te.js": "../../../../moment/locale/te.js",
	"./tet": "../../../../moment/locale/tet.js",
	"./tet.js": "../../../../moment/locale/tet.js",
	"./th": "../../../../moment/locale/th.js",
	"./th.js": "../../../../moment/locale/th.js",
	"./tl-ph": "../../../../moment/locale/tl-ph.js",
	"./tl-ph.js": "../../../../moment/locale/tl-ph.js",
	"./tlh": "../../../../moment/locale/tlh.js",
	"./tlh.js": "../../../../moment/locale/tlh.js",
	"./tr": "../../../../moment/locale/tr.js",
	"./tr.js": "../../../../moment/locale/tr.js",
	"./tzl": "../../../../moment/locale/tzl.js",
	"./tzl.js": "../../../../moment/locale/tzl.js",
	"./tzm": "../../../../moment/locale/tzm.js",
	"./tzm-latn": "../../../../moment/locale/tzm-latn.js",
	"./tzm-latn.js": "../../../../moment/locale/tzm-latn.js",
	"./tzm.js": "../../../../moment/locale/tzm.js",
	"./uk": "../../../../moment/locale/uk.js",
	"./uk.js": "../../../../moment/locale/uk.js",
	"./ur": "../../../../moment/locale/ur.js",
	"./ur.js": "../../../../moment/locale/ur.js",
	"./uz": "../../../../moment/locale/uz.js",
	"./uz-latn": "../../../../moment/locale/uz-latn.js",
	"./uz-latn.js": "../../../../moment/locale/uz-latn.js",
	"./uz.js": "../../../../moment/locale/uz.js",
	"./vi": "../../../../moment/locale/vi.js",
	"./vi.js": "../../../../moment/locale/vi.js",
	"./x-pseudo": "../../../../moment/locale/x-pseudo.js",
	"./x-pseudo.js": "../../../../moment/locale/x-pseudo.js",
	"./yo": "../../../../moment/locale/yo.js",
	"./yo.js": "../../../../moment/locale/yo.js",
	"./zh-cn": "../../../../moment/locale/zh-cn.js",
	"./zh-cn.js": "../../../../moment/locale/zh-cn.js",
	"./zh-hk": "../../../../moment/locale/zh-hk.js",
	"./zh-hk.js": "../../../../moment/locale/zh-hk.js",
	"./zh-tw": "../../../../moment/locale/zh-tw.js",
	"./zh-tw.js": "../../../../moment/locale/zh-tw.js"
};
function webpackContext(req) {
	return __webpack_require__(webpackContextResolve(req));
};
function webpackContextResolve(req) {
	var id = map[req];
	if(!(id + 1)) // check for number or string
		throw new Error("Cannot find module '" + req + "'.");
	return id;
};
webpackContext.keys = function webpackContextKeys() {
	return Object.keys(map);
};
webpackContext.resolve = webpackContextResolve;
module.exports = webpackContext;
webpackContext.id = "../../../../moment/locale recursive ^\\.\\/.*$";

/***/ }),

/***/ 1:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("../../../../../src/main.ts");


/***/ })

},[1]);
//# sourceMappingURL=main.bundle.js.map