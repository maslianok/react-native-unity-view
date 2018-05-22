"use strict";
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
var __assign = (this && this.__assign) || Object.assign || function(t) {
    for (var s, i = 1, n = arguments.length; i < n; i++) {
        s = arguments[i];
        for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
            t[p] = s[p];
    }
    return t;
};
var __rest = (this && this.__rest) || function (s, e) {
    var t = {};
    for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p) && e.indexOf(p) < 0)
        t[p] = s[p];
    if (s != null && typeof Object.getOwnPropertySymbols === "function")
        for (var i = 0, p = Object.getOwnPropertySymbols(s); i < p.length; i++) if (e.indexOf(p[i]) < 0)
            t[p[i]] = s[p[i]];
    return t;
};
exports.__esModule = true;
var React = require("react");
var react_native_1 = require("react-native");
var PropTypes = require("prop-types");
var ViewPropTypes = require("react-native/Libraries/Components/View/ViewPropTypes");
var UIManager = react_native_1.NativeModules.UIManager;
var UNITY_VIEW_REF = 'unityview';
var UnityView = /** @class */ (function (_super) {
    __extends(UnityView, _super);
    function UnityView() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    /**
     * Send Message to Unity.
     * @param gameObject The Name of GameObject. Also can be a path string.
     * @param methodName Method name in GameObject instance.
     * @param message The message will post.
     */
    UnityView.prototype.postMessage = function (gameObject, methodName, message) {
        UIManager.dispatchViewManagerCommand(this.getViewHandle(), UIManager.UnityView.Commands.postMessage, [String(gameObject), String(methodName), String(message)]);
    };

    UnityView.prototype.pause = function () {
        UIManager.dispatchViewManagerCommand(this.getViewHandle(), UIManager.UnityView.Commands.pause, []);
    };

    UnityView.prototype.resume = function () {
        UIManager.dispatchViewManagerCommand(this.getViewHandle(), UIManager.UnityView.Commands.resume, []);
    };
    ;
    /**
     * Send Message to UnityMessageManager.
     * @param message The message will post.
     */
    UnityView.prototype.postMessageToUnityManager = function (message) {
        this.postMessage('UnityMessageManager', 'onMessage', message);
    };
    ;
    UnityView.prototype.getViewHandle = function () {
        return react_native_1.findNodeHandle(this.refs[UNITY_VIEW_REF]);
    };
    UnityView.prototype.onMessage = function (event) {
        if (this.props.onMessage) {
            this.props.onMessage(event);
        }
    };
    UnityView.prototype.render = function () {
        var props = __rest(this.props, []);
        return (React.createElement(NativeUnityView, __assign({ ref: UNITY_VIEW_REF }, props, { onMessage: this.onMessage.bind(this) })));
    };
    UnityView.propTypes = __assign({}, ViewPropTypes, { onMessage: PropTypes.func });
    return UnityView;
}(React.Component));
exports["default"] = UnityView;
var NativeUnityView = react_native_1.requireNativeComponent('UnityView', UnityView);
//# sourceMappingURL=index.js.map