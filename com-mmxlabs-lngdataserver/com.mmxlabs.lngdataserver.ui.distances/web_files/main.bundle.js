webpackJsonp([1],{

/***/ "../../../../../generated/ports-distances/api.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-distances/configuration.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_countries_service__ = __webpack_require__("../../../../../generated/ports-distances/api/countries.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__api_distances_service__ = __webpack_require__("../../../../../generated/ports-distances/api/distances.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__api_ports_service__ = __webpack_require__("../../../../../generated/ports-distances/api/ports.service.ts");
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
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["b" /* NgModule */])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_common__["a" /* CommonModule */], __WEBPACK_IMPORTED_MODULE_2__angular_http__["a" /* HttpModule */]],
        declarations: [],
        exports: [],
        providers: [__WEBPACK_IMPORTED_MODULE_4__api_countries_service__["a" /* CountriesService */], __WEBPACK_IMPORTED_MODULE_5__api_distances_service__["a" /* DistancesService */], __WEBPACK_IMPORTED_MODULE_6__api_ports_service__["a" /* PortsService */]]
    })
], ApiModule);

var ApiModule_1;
//# sourceMappingURL=api.module.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/api/api.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__countries_service__ = __webpack_require__("../../../../../generated/ports-distances/api/countries.service.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__distances_service__ = __webpack_require__("../../../../../generated/ports-distances/api/distances.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "b", function() { return __WEBPACK_IMPORTED_MODULE_1__distances_service__["a"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ports_service__ = __webpack_require__("../../../../../generated/ports-distances/api/ports.service.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "a", function() { return __WEBPACK_IMPORTED_MODULE_2__ports_service__["a"]; });
/* unused harmony export APIS */






var APIS = [__WEBPACK_IMPORTED_MODULE_0__countries_service__["a" /* CountriesService */], __WEBPACK_IMPORTED_MODULE_1__distances_service__["a" /* DistancesService */], __WEBPACK_IMPORTED_MODULE_2__ports_service__["a" /* PortsService */]];
//# sourceMappingURL=api.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/api/countries.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__rxjs_operators__ = __webpack_require__("../../../../../generated/ports-distances/rxjs-operators.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports-distances/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports-distances/configuration.ts");
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






var CountriesService = (function () {
    function CountriesService(http, basePath, configuration) {
        this.http = http;
        this.basePath = 'http://localhost:8090';
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
    CountriesService.prototype.extendObj = function (objA, objB) {
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
    /**
     * GET all countries
     *
     */
    CountriesService.prototype.getCountriesUsingGET = function (extraHttpRequestParams) {
        return this.getCountriesUsingGETWithHttpInfo(extraHttpRequestParams)
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
     * GET all countries
     *
     */
    CountriesService.prototype.getCountriesUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/countries';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            '*/*'
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
    return CountriesService;
}());
CountriesService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["d" /* Injectable */])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["f" /* Inject */])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], CountriesService);

var _a, _b;
//# sourceMappingURL=countries.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/api/distances.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__rxjs_operators__ = __webpack_require__("../../../../../generated/ports-distances/rxjs-operators.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports-distances/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports-distances/configuration.ts");
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






var DistancesService = (function () {
    function DistancesService(http, basePath, configuration) {
        this.http = http;
        this.basePath = 'https://localhost:8080';
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
    DistancesService.prototype.extendObj = function (objA, objB) {
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
    /**
     * GET distance matrix for all ports
     * Retrieve distance information for all available ports in either JSON or CSV format.    JSON format is Source Port-&gt;(Destination Port-&gt;Distance), e.g.   {\&quot;Chiba\&quot;: { \&quot;Copenhagen\&quot;: \&quot;15365.657\&quot;, \&quot;Melbourne\&quot;: \&quot;4962.625\&quot;}, \&quot;Copenhagen\&quot;:...}    CSV is a matrix in the format:   from,Chiba,Copenhagen,Melbourne   Chiba,0,15365.657,4962.625   Copenhagen,15365.657,0,12826.226   Melbourne,4962.735,12826.123,0    The content type can be changed by sending one of the following headers with the request: &#x60;Accept: text/csv&#x60; or &#x60;Accept: application/json&#x60;
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getDistanceMatrixUsingGET = function (open, v, extraHttpRequestParams) {
        return this.getDistanceMatrixUsingGETWithHttpInfo(open, v, extraHttpRequestParams)
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
     * GET a single distance
     * Returns the distance for a voyage __potentially__ passing open Routing Points. Refer to  more the /distance/route/ endpoint if more information about the route taken is needed.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getDistanceUsingGET = function (srcPort, dstPort, open, v, extraHttpRequestParams) {
        return this.getDistanceUsingGETWithHttpInfo(srcPort, dstPort, open, v, extraHttpRequestParams)
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
     * GET a single route
     * Returns the distance for a voyage including information which routing points are passed.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getRouteUsingGET = function (srcPort, dstPort, open, v, extraHttpRequestParams) {
        return this.getRouteUsingGETWithHttpInfo(srcPort, dstPort, open, v, extraHttpRequestParams)
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
     * GET all available versions
     *
     */
    DistancesService.prototype.getVersionsUsingGET = function (extraHttpRequestParams) {
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
     * PUT a single distance
     * Update a distance. Only direct distances can be updated since distances through a routing point are computed based on the direct distances.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param distanceEditRequest The new distance
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.updateDistanceUsingPUT = function (srcPort, dstPort, distanceEditRequest, v, extraHttpRequestParams) {
        return this.updateDistanceUsingPUTWithHttpInfo(srcPort, dstPort, distanceEditRequest, v, extraHttpRequestParams)
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
     * GET distance matrix for all ports
     * Retrieve distance information for all available ports in either JSON or CSV format.    JSON format is Source Port-&gt;(Destination Port-&gt;Distance), e.g.   {\&quot;Chiba\&quot;: { \&quot;Copenhagen\&quot;: \&quot;15365.657\&quot;, \&quot;Melbourne\&quot;: \&quot;4962.625\&quot;}, \&quot;Copenhagen\&quot;:...}    CSV is a matrix in the format:   from,Chiba,Copenhagen,Melbourne   Chiba,0,15365.657,4962.625   Copenhagen,15365.657,0,12826.226   Melbourne,4962.735,12826.123,0    The content type can be changed by sending one of the following headers with the request: &#x60;Accept: text/csv&#x60; or &#x60;Accept: application/json&#x60;
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getDistanceMatrixUsingGETWithHttpInfo = function (open, v, extraHttpRequestParams) {
        var path = this.basePath + '/distances';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        if (open !== undefined) {
            queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        // to determine the Accept header
        var produces = [
            'application/json',
            'text/csv'
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
     * GET a single distance
     * Returns the distance for a voyage __potentially__ passing open Routing Points. Refer to  more the /distance/route/ endpoint if more information about the route taken is needed.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getDistanceUsingGETWithHttpInfo = function (srcPort, dstPort, open, v, extraHttpRequestParams) {
        var path = this.basePath + '/distance/${srcPort}/${dstPort}'
            .replace('${' + 'srcPort' + '}', String(srcPort))
            .replace('${' + 'dstPort' + '}', String(dstPort));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'srcPort' is not null or undefined
        if (srcPort === null || srcPort === undefined) {
            throw new Error('Required parameter srcPort was null or undefined when calling getDistanceUsingGET.');
        }
        // verify required parameter 'dstPort' is not null or undefined
        if (dstPort === null || dstPort === undefined) {
            throw new Error('Required parameter dstPort was null or undefined when calling getDistanceUsingGET.');
        }
        if (open !== undefined) {
            queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        // to determine the Accept header
        var produces = [
            '*/*'
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
     * GET a single route
     * Returns the distance for a voyage including information which routing points are passed.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param open A comma separated list of open routing points. Refer to [Available Routing Points](#available-routing-points) to see all available options.   __Default__ is no open routing points.     __Example__: PAN,SUZ     __Example__: PAN
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.getRouteUsingGETWithHttpInfo = function (srcPort, dstPort, open, v, extraHttpRequestParams) {
        var path = this.basePath + '/distance/route/${srcPort}/${dstPort}'
            .replace('${' + 'srcPort' + '}', String(srcPort))
            .replace('${' + 'dstPort' + '}', String(dstPort));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'srcPort' is not null or undefined
        if (srcPort === null || srcPort === undefined) {
            throw new Error('Required parameter srcPort was null or undefined when calling getRouteUsingGET.');
        }
        // verify required parameter 'dstPort' is not null or undefined
        if (dstPort === null || dstPort === undefined) {
            throw new Error('Required parameter dstPort was null or undefined when calling getRouteUsingGET.');
        }
        if (open !== undefined) {
            queryParameters.set('open', open);
        }
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        // to determine the Accept header
        var produces = [
            '*/*'
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
     * GET all available versions
     *
     */
    DistancesService.prototype.getVersionsUsingGETWithHttpInfo = function (extraHttpRequestParams) {
        var path = this.basePath + '/distances/versions';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // to determine the Accept header
        var produces = [
            '*/*'
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
     * PUT a single distance
     * Update a distance. Only direct distances can be updated since distances through a routing point are computed based on the direct distances.
     * @param srcPort __Source Port__, either a mmxId or a name
     * @param dstPort __Destination Port__, either a mmxId or a name
     * @param distanceEditRequest The new distance
     * @param v The version of the distance. Ignore to get latest version.
     */
    DistancesService.prototype.updateDistanceUsingPUTWithHttpInfo = function (srcPort, dstPort, distanceEditRequest, v, extraHttpRequestParams) {
        var path = this.basePath + '/distance/${srcPort}/${dstPort}'
            .replace('${' + 'srcPort' + '}', String(srcPort))
            .replace('${' + 'dstPort' + '}', String(dstPort));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'srcPort' is not null or undefined
        if (srcPort === null || srcPort === undefined) {
            throw new Error('Required parameter srcPort was null or undefined when calling updateDistanceUsingPUT.');
        }
        // verify required parameter 'dstPort' is not null or undefined
        if (dstPort === null || dstPort === undefined) {
            throw new Error('Required parameter dstPort was null or undefined when calling updateDistanceUsingPUT.');
        }
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        // to determine the Accept header
        var produces = [
            '*/*'
        ];
        headers.set('Content-Type', 'application/json');
        var requestOptions = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["d" /* RequestOptions */]({
            method: __WEBPACK_IMPORTED_MODULE_1__angular_http__["e" /* RequestMethod */].Put,
            headers: headers,
            body: distanceEditRequest == null ? '' : JSON.stringify(distanceEditRequest),
            search: queryParameters,
            withCredentials: this.configuration.withCredentials
        });
        // https://github.com/swagger-api/swagger-codegen/issues/4037
        if (extraHttpRequestParams) {
            requestOptions = Object.assign(requestOptions, extraHttpRequestParams);
        }
        return this.http.request(path, requestOptions);
    };
    return DistancesService;
}());
DistancesService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["d" /* Injectable */])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["f" /* Inject */])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], DistancesService);

var _a, _b;
//# sourceMappingURL=distances.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/api/ports.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__rxjs_operators__ = __webpack_require__("../../../../../generated/ports-distances/rxjs-operators.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__variables__ = __webpack_require__("../../../../../generated/ports-distances/variables.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__configuration__ = __webpack_require__("../../../../../generated/ports-distances/configuration.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PortsService; });
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






var PortsService = (function () {
    function PortsService(http, basePath, configuration) {
        this.http = http;
        this.basePath = 'http://localhost:8090';
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
    PortsService.prototype.extendObj = function (objA, objB) {
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
    PortsService.prototype.canConsumeForm = function (consumes) {
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
     * GET a port
     *
     * @param port port
     * @param v The version of the ports. Ignore to get latest version.
     * @param fuzzy Whether or not fuzzy matching should be applied. For example, Barcelone -&gt; Barcelona
     */
    PortsService.prototype.getPortUsingGET = function (port, v, fuzzy, extraHttpRequestParams) {
        return this.getPortUsingGETWithHttpInfo(port, v, fuzzy, extraHttpRequestParams)
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
     * @param v The version of the ports. Ignore to get latest version.
     */
    PortsService.prototype.getPortsUsingGET = function (v, extraHttpRequestParams) {
        return this.getPortsUsingGETWithHttpInfo(v, extraHttpRequestParams)
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
     * @param port port
     * @param v The version of the ports. Ignore to get latest version.
     * @param fuzzy Whether or not fuzzy matching should be applied. For example, Barcelone -&gt; Barcelona
     */
    PortsService.prototype.getPortUsingGETWithHttpInfo = function (port, v, fuzzy, extraHttpRequestParams) {
        var path = this.basePath + '/ports/${port}'
            .replace('${' + 'port' + '}', String(port));
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        // verify required parameter 'port' is not null or undefined
        if (port === null || port === undefined) {
            throw new Error('Required parameter port was null or undefined when calling getPortUsingGET.');
        }
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        if (fuzzy !== undefined) {
            queryParameters.set('fuzzy', fuzzy);
        }
        // to determine the Accept header
        var produces = [
            '*/*'
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
     * GET all ports
     *
     * @param v The version of the ports. Ignore to get latest version.
     */
    PortsService.prototype.getPortsUsingGETWithHttpInfo = function (v, extraHttpRequestParams) {
        var path = this.basePath + '/ports';
        var queryParameters = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["c" /* URLSearchParams */]();
        var headers = new __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Headers */](this.defaultHeaders.toJSON()); // https://github.com/angular/angular/issues/6845
        if (v !== undefined) {
            queryParameters.set('v', v);
        }
        // to determine the Accept header
        var produces = [
            '*/*'
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
    return PortsService;
}());
PortsService = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["d" /* Injectable */])(),
    __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()), __param(1, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["f" /* Inject */])(__WEBPACK_IMPORTED_MODULE_3__variables__["a" /* BASE_PATH */])), __param(2, __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["e" /* Optional */])()),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["f" /* Http */]) === "function" && _a || Object, String, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4__configuration__["a" /* Configuration */]) === "function" && _b || Object])
], PortsService);

var _a, _b;
//# sourceMappingURL=ports.service.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/configuration.ts":
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

/***/ "../../../../../generated/ports-distances/index.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__api_api__ = __webpack_require__("../../../../../generated/ports-distances/api/api.ts");
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "PortsService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["a"]; });
/* harmony namespace reexport (by used) */ __webpack_require__.d(__webpack_exports__, "DistancesService", function() { return __WEBPACK_IMPORTED_MODULE_0__api_api__["b"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__model_models__ = __webpack_require__("../../../../../generated/ports-distances/model/models.ts");
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__model_models__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_1__model_models__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__variables__ = __webpack_require__("../../../../../generated/ports-distances/variables.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__configuration__ = __webpack_require__("../../../../../generated/ports-distances/configuration.ts");
/* unused harmony namespace reexport */
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__api_module__ = __webpack_require__("../../../../../generated/ports-distances/api.module.ts");
/* unused harmony namespace reexport */





//# sourceMappingURL=index.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/model/country.ts":
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
//# sourceMappingURL=country.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/model/distanceEditRequest.ts":
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

/***/ "../../../../../generated/ports-distances/model/identifier.ts":
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

/***/ "../../../../../generated/ports-distances/model/location.ts":
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
//# sourceMappingURL=location.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/model/models.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__country__ = __webpack_require__("../../../../../generated/ports-distances/model/country.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__country___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__country__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_0__country__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_0__country__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__ = __webpack_require__("../../../../../generated/ports-distances/model/distanceEditRequest.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_1__distanceEditRequest__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__identifier__ = __webpack_require__("../../../../../generated/ports-distances/model/identifier.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__identifier___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__identifier__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_2__identifier__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_2__identifier__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__location__ = __webpack_require__("../../../../../generated/ports-distances/model/location.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__location___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__location__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_3__location__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_3__location__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__port__ = __webpack_require__("../../../../../generated/ports-distances/model/port.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__port___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__port__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_4__port__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_4__port__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__route__ = __webpack_require__("../../../../../generated/ports-distances/model/route.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__route___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__route__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_5__route__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_5__route__["Port"]; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__version__ = __webpack_require__("../../../../../generated/ports-distances/model/version.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__version___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__version__);
/* harmony namespace reexport (by used) */ if(__webpack_require__.o(__WEBPACK_IMPORTED_MODULE_6__version__, "Port")) __webpack_require__.d(__webpack_exports__, "Port", function() { return __WEBPACK_IMPORTED_MODULE_6__version__["Port"]; });







//# sourceMappingURL=models.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/model/port.ts":
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
//# sourceMappingURL=port.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/model/route.ts":
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

/***/ "../../../../../generated/ports-distances/model/version.ts":
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
//# sourceMappingURL=version.js.map

/***/ }),

/***/ "../../../../../generated/ports-distances/rxjs-operators.ts":
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

/***/ "../../../../../generated/ports-distances/variables.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return BASE_PATH; });
/* unused harmony export COLLECTION_FORMATS */

var BASE_PATH = new __WEBPACK_IMPORTED_MODULE_0__angular_core__["c" /* InjectionToken */]('basePath');
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
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__port_detail_port_detail_component__ = __webpack_require__("../../../../../src/app/port-detail/port-detail.component.ts");
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppRoutingModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};





var routes = [
    { path: '', redirectTo: '/distances', pathMatch: 'full' },
    { path: 'distances', component: __WEBPACK_IMPORTED_MODULE_2__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */] },
    { path: 'distances/:version', component: __WEBPACK_IMPORTED_MODULE_2__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */] },
    { path: 'ports/:id', component: __WEBPACK_IMPORTED_MODULE_4__port_detail_port_detail_component__["a" /* PortDetailComponent */] },
    { path: 'ports', component: __WEBPACK_IMPORTED_MODULE_3__ports_ports_component__["a" /* PortsComponent */] }
];
var AppRoutingModule = (function () {
    function AppRoutingModule() {
    }
    return AppRoutingModule;
}());
AppRoutingModule = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["b" /* NgModule */])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* RouterModule */].forRoot(routes)],
        exports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* RouterModule */]]
    })
], AppRoutingModule);

//# sourceMappingURL=app-routing.module.js.map

/***/ }),

/***/ "../../../../../src/app/app.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "h1 {\n  font-size: 1.2em;\n  color: #999;\n  margin-bottom: 0;\n}\nh2 {\n  font-size: 2em;\n  margin-top: 0;\n  padding-top: 0;\n}\nnav a {\n  padding: 5px 10px;\n  text-decoration: none;\n  margin-top: 10px;\n  display: inline-block;\n  background-color: #eee;\n  border-radius: 4px;\n}\nnav a:visited, a:link {\n  color: #607D8B;\n}\nnav a:hover {\n  color: #039be5;\n  background-color: #CFD8DC;\n}\nnav a.active {\n  color: #039be5;\n}", ""]);

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
        this.title = 'Ports and Distances';
    }
    return AppComponent;
}());
AppComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_0" /* Component */])({
        selector: 'my-app',
        template: "\n    <h1>{{title}}</h1>\n    <nav>\n        <a routerLink=\"/distances\" routerLinkActive=\"active\">Distances</a>\n        <a routerLink=\"/ports\" routerLinkActive=\"active\">Ports</a>\n    </nav>\n    <router-outlet></router-outlet>\n ",
        styles: [__webpack_require__("../../../../../src/app/app.component.css")]
    })
], AppComponent);

//# sourceMappingURL=app.component.js.map

/***/ }),

/***/ "../../../../../src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__("../../../platform-browser/@angular/platform-browser.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_forms__ = __webpack_require__("../../../forms/@angular/forms.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__app_routing_module__ = __webpack_require__("../../../../../src/app/app-routing.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6_ng2_handsontable__ = __webpack_require__("../../../../ng2-handsontable/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__generated_ports_distances_api_module__ = __webpack_require__("../../../../../generated/ports-distances/api.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__distance_matrix_distance_matrix_component__ = __webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__ports_ports_component__ = __webpack_require__("../../../../../src/app/ports/ports.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__port_detail_port_detail_component__ = __webpack_require__("../../../../../src/app/port-detail/port-detail.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__generated_ports_distances_variables__ = __webpack_require__("../../../../../generated/ports-distances/variables.ts");
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
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}
function ApiBaseUrlFactory() {
    if (location.search.includes('apiBaseUrl')) {
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
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["b" /* NgModule */])({
        declarations: [
            __WEBPACK_IMPORTED_MODULE_3__app_component__["a" /* AppComponent */],
            __WEBPACK_IMPORTED_MODULE_8__distance_matrix_distance_matrix_component__["a" /* DistanceMatrixComponent */],
            __WEBPACK_IMPORTED_MODULE_9__ports_ports_component__["a" /* PortsComponent */],
            __WEBPACK_IMPORTED_MODULE_10__port_detail_port_detail_component__["a" /* PortDetailComponent */],
        ],
        imports: [
            __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
            __WEBPACK_IMPORTED_MODULE_2__angular_forms__["a" /* FormsModule */],
            __WEBPACK_IMPORTED_MODULE_4__app_routing_module__["a" /* AppRoutingModule */],
            __WEBPACK_IMPORTED_MODULE_5__angular_http__["a" /* HttpModule */],
            __WEBPACK_IMPORTED_MODULE_6_ng2_handsontable__["a" /* HotTableModule */],
            __WEBPACK_IMPORTED_MODULE_7__generated_ports_distances_api_module__["a" /* ApiModule */]
        ],
        providers: [
            [{ provide: __WEBPACK_IMPORTED_MODULE_11__generated_ports_distances_variables__["a" /* BASE_PATH */], useFactory: ApiBaseUrlFactory }]
        ],
        bootstrap: [__WEBPACK_IMPORTED_MODULE_3__app_component__["a" /* AppComponent */]]
    })
], AppModule);

//# sourceMappingURL=app.module.js.map

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.html":
/***/ (function(module, exports) {

module.exports = "<label>From:</label> <input type=\"text\" (input)=\"fromChange($event.target.value)\" />\n<label>To:</label> <input type=\"text\" (input)=\"toChange($event.target.value)\" />\n\n\n<hotTable [data]=\"data\"\n          (after-change)=\"afterChange($event)\"\n          (after-on-cell-mouse-down)=\"afterOnCellMouseDown($event)\"\n          [colHeaders]=\"colHeaders\"\n          [options]=\"options\"\n          [colWidths]=\"colWidths\">\n</hotTable>"

/***/ }),

/***/ "../../../../../src/app/distance-matrix/distance-matrix.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__ = __webpack_require__("../../../../../generated/ports-distances/index.ts");
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
    function DistanceMatrixComponent(route, portsService, distancesService) {
        var _this = this;
        this.route = route;
        this.portsService = portsService;
        this.distancesService = distancesService;
        this.data = new Array();
        this.colHeaders = new Array();
        this.ports = new Array();
        // name --> mmxId
        this.portMap = new Map();
        if (route.snapshot.paramMap.has("version")) {
            this.version = route.snapshot.paramMap.get("version");
        }
        this.options = {
            height: 700,
            rowHeaders: new Array(),
            rowHeaderWidth: 180,
            stretchH: 'all',
            columnSorting: true,
            contextMenu: true,
            className: 'htCenter htMiddle',
            afterChange: function (changes) {
                if (changes == null) {
                    // prevent update calls when building table
                    return;
                }
                _this.updateDistance(_this.portForRow(changes[0][0]), _this.portForColumn(changes[0][1]), changes[0][3]);
            }
        };
    }
    DistanceMatrixComponent.prototype.ngOnInit = function () {
        this.fetchUpstream();
    };
    DistanceMatrixComponent.prototype.fetchUpstream = function () {
        var _this = this;
        this.portsService.getPortsUsingGET(this.version, undefined).toPromise()
            .then(function (ports) {
            _this.ports = ports.filter(function (p) { return p.virtual == false; });
            _this.data = new Array(_this.ports.length);
            for (var _i = 0; _i < _this.data.length; _i++) {
                _this.data[_i] = new Array(_this.ports.length);
            }
            _this.ports.filter(function (p) { return p.virtual == false; }).forEach(function (p) {
                _this.portMap.set(p.name, p.mmxId);
                _this.colHeaders.push(p.name);
                _this.options.rowHeaders.push(p.name);
            });
        })
            .catch(function (error) { return console.log(error); });
        this.distancesService.getDistanceMatrixUsingGET(undefined, this.version, undefined).toPromise()
            .then(function (distances) {
            _this.distances = distances;
            for (var i = 0; i < _this.colHeaders.length; i++) {
                _this.data[i] = new Array();
                for (var j = 0; j < _this.colHeaders.length; j++) {
                    _this.data[i][j] = _this.distances[_this.portMap.get(_this.options.rowHeaders[i])][_this.portMap.get(_this.colHeaders[j])];
                }
            }
        });
    };
    DistanceMatrixComponent.prototype.portForRow = function (row) {
        var _this = this;
        return this.ports.find(function (p) { return p.name === _this.options.rowHeaders[row.toString()]; });
    };
    DistanceMatrixComponent.prototype.portForColumn = function (column) {
        var _this = this;
        return this.ports.find(function (p) { return p.name === _this.colHeaders[column.toString()]; });
    };
    DistanceMatrixComponent.prototype.updateDistance = function (from, to, distance) {
        this.distancesService.updateDistanceUsingPUT(from.mmxId, to.mmxId, { distance: distance, provider: "Web UI" }).toPromise()
            .then(function (result) { return console.log("successfully updated"); })
            .catch(function (error) { return console.log(error); });
        console.log("zee update: " + from.name + " -> " + to.name + ": " + distance);
    };
    DistanceMatrixComponent.prototype.filterColumn = function (searchString) {
        var filteredPorts = this.ports.filter(function (p) { return p.name.toLowerCase().startsWith(searchString.toLowerCase()); });
        // let filteredColHeaders = this.colHeaders.filter(e => e.startsWith(searchString))
        var filteredData = new Array();
        for (var i = 0; i < this.options.rowHeaders.length; i++) {
            filteredData[i] = new Array();
            for (var j = 0; j < filteredPorts.length; j++) {
                filteredData[i][j] = this.distances[this.portMap.get(this.options.rowHeaders[i])][filteredPorts[j].mmxId];
            }
        }
        this.colHeaders = filteredPorts.map(function (p) { return p.name; });
        this.data = filteredData;
        this.options = Object.assign({}, this.options);
    };
    DistanceMatrixComponent.prototype.filterRow = function (searchString) {
        var filteredPorts = this.ports.filter(function (p) { return p.name.toLowerCase().startsWith(searchString.toLowerCase()); });
        // let filteredColHeaders = this.colHeaders.filter(e => e.startsWith(searchString))
        var filteredData = new Array();
        for (var i = 0; i < filteredPorts.length; i++) {
            filteredData[i] = new Array();
            for (var j = 0; j < this.colHeaders.length; j++) {
                filteredData[i][j] = this.distances[filteredPorts[i].mmxId][this.portMap.get(this.colHeaders[j])];
            }
        }
        this.options.rowHeaders = filteredPorts.map(function (p) { return p.name; });
        this.data = filteredData;
        this.options = Object.assign({}, this.options);
    };
    DistanceMatrixComponent.prototype.fromChange = function (seachString) {
        this.filterRow(seachString);
    };
    DistanceMatrixComponent.prototype.toChange = function (seachString) {
        this.filterColumn(seachString);
    };
    return DistanceMatrixComponent;
}());
DistanceMatrixComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_0" /* Component */])({
        selector: 'app-distance-matrix',
        template: __webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.html"),
        styles: [__webpack_require__("../../../../../src/app/distance-matrix/distance-matrix.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["b" /* ActivatedRoute */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__angular_router__["b" /* ActivatedRoute */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["PortsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["PortsService"]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["DistancesService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["DistancesService"]) === "function" && _c || Object])
], DistanceMatrixComponent);

var _a, _b, _c;
//# sourceMappingURL=distance-matrix.component.js.map

/***/ }),

/***/ "../../../../../src/app/port-detail/port-detail.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/port-detail/port-detail.component.html":
/***/ (function(module, exports) {

module.exports = "<div *ngIf=\"hero\">\n    <h2>{{hero.name}} details!</h2>\n    <div><label>id: </label>{{hero.id}}</div>\n    <div>\n      <label>name: </label>\n      <input [(ngModel)]=\"hero.name\" placeholder=\"name\"/>\n    </div>\n    <button (click)=\"goBack()\">Back</button>\n    <button (click)=\"save()\">Save</button>\n</div>"

/***/ }),

/***/ "../../../../../src/app/port-detail/port-detail.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common__ = __webpack_require__("../../../common/@angular/common.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__generated_ports_distances_index__ = __webpack_require__("../../../../../generated/ports-distances/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap__ = __webpack_require__("../../../../rxjs/add/operator/switchMap.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_rxjs_add_operator_switchMap__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PortDetailComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var PortDetailComponent = (function () {
    function PortDetailComponent(route, location, portsService) {
        this.route = route;
        this.location = location;
        this.portsService = portsService;
    }
    PortDetailComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.route.paramMap
            .switchMap(function (params) { return _this.portsService.getPortUsingGET(params.get('id')); })
            .subscribe(function (port) { return _this.port = port; });
    };
    PortDetailComponent.prototype.goBack = function () {
        this.location.back();
    };
    PortDetailComponent.prototype.save = function () {
        //
    };
    return PortDetailComponent;
}());
__decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["O" /* Input */])(),
    __metadata("design:type", typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__generated_ports_distances_index__["Port"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__generated_ports_distances_index__["Port"]) === "function" && _a || Object)
], PortDetailComponent.prototype, "port", void 0);
PortDetailComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_0" /* Component */])({
        selector: 'app-port-detail',
        template: __webpack_require__("../../../../../src/app/port-detail/port-detail.component.html"),
        styles: [__webpack_require__("../../../../../src/app/port-detail/port-detail.component.css")]
    }),
    __metadata("design:paramtypes", [typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* ActivatedRoute */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_2__angular_common__["g" /* Location */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_common__["g" /* Location */]) === "function" && _c || Object, typeof (_d = typeof __WEBPACK_IMPORTED_MODULE_3__generated_ports_distances_index__["PortsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_3__generated_ports_distances_index__["PortsService"]) === "function" && _d || Object])
], PortDetailComponent);

var _a, _b, _c, _d;
//# sourceMappingURL=port-detail.component.js.map

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "selected {\n    background-color: #CFD8DC !important;\n    color: white;\n  }\n  .ports {\n    margin: 0 0 2em 0;\n    list-style-type: none;\n    padding: 0;\n    width: 20em;\n  }\n  .ports li {\n    cursor: pointer;\n    position: relative;\n    left: 0;\n    background-color: #EEE;\n    margin: .5em;\n    padding: .3em 0;\n    height: 1.6em;\n    border-radius: 4px;\n  }\n  .ports li:hover {\n    color: #607D8B;\n    background-color: #DDD;\n    left: .1em;\n  }\n  .ports li.selected:hover {\n    background-color: #BBD8DC !important;\n    color: white;\n  }\n  .ports .text {\n    position: relative;\n    top: -3px;\n  }\n  .ports .badge {\n    display: inline-block;\n    font-size: small;\n    color: white;\n    padding: 0.8em 0.7em 0 0.7em;\n    background-color: #607D8B;\n    line-height: 1em;\n    position: relative;\n    left: -1px;\n    top: -4px;\n    height: 1.8em;\n    width: 8em;\n    margin-right: .8em;\n    border-radius: 4px 0 0 4px;\n  }\n  button {\n    font-family: Arial;\n    background-color: #eee;\n    border: none;\n    padding: 5px 10px;\n    border-radius: 4px;\n    cursor: pointer;\n    cursor: hand;\n  }\n  button:hover {\n    background-color: #cfd8dc;\n  }\n\n  button.delete {\n    float:right;\n    margin-top: 2px;\n    margin-right: .8em;\n    background-color: gray !important;\n    color:white;\n  }", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.html":
/***/ (function(module, exports) {

module.exports = "<h2>Ports</h2>\n<ul class=\"ports\">\n    <li *ngFor=\"let port of ports\" (click)=\"onSelect(port)\" [class.selected]=\"port === selectedPort\">\n        <span class=\"badge\">{{port.location.country}}</span> {{port.name}}\n    </li>\n\n</ul>\n<div *ngIf=\"selectedPort\">\n    <h2>\n        {{selectedPort.name | uppercase}} is my hero\n    </h2>\n    <button (click)=\"gotoDetail()\">View Details</button>\n</div>"

/***/ }),

/***/ "../../../../../src/app/ports/ports.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__ = __webpack_require__("../../../../../generated/ports-distances/index.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
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
    function PortsComponent(router, portsService) {
        this.router = router;
        this.portsService = portsService;
    }
    PortsComponent.prototype.ngOnInit = function () {
        this.getPorts();
    };
    PortsComponent.prototype.getPorts = function () {
        var _this = this;
        this.portsService.getPortsUsingGET().toPromise()
            .then(function (ports) { return _this.ports = ports; })
            .catch(function (error) { return console.log(error); });
    };
    PortsComponent.prototype.gotoDetail = function (selectedPort) {
        this.router.navigate(['/detail', selectedPort.mmxId]);
    };
    return PortsComponent;
}());
PortsComponent = __decorate([
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_0" /* Component */])({
        selector: 'app-ports',
        template: __webpack_require__("../../../../../src/app/ports/ports.component.html"),
        styles: [__webpack_require__("../../../../../src/app/ports/ports.component.css")],
        providers: [__WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["PortsService"]]
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__angular_router__["c" /* Router */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_2__angular_router__["c" /* Router */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["PortsService"] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__generated_ports_distances_index__["PortsService"]) === "function" && _b || Object])
], PortsComponent);

var _a, _b;
//# sourceMappingURL=ports.component.js.map

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
    __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["a" /* enableProdMode */])();
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

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("../../../../../src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map