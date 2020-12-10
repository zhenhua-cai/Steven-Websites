let specialCharsList =[];
let allowedSpecialChars =  ',.@#%&;:[]{}()+=_-/';
let passwordLength = 0;
const minPasswordLength = 6;
const maxPasswordLength = 30;

//starts username validation when key press
const maxUsernameLength = 30;

let validationErrors = {
    username: {},
    password: {},
    passwordConfirm: {}
};

for(let char of allowedSpecialChars){
    specialCharsList.push(char);
}

function listSpecialChars(specialCharClass, parentElement) {
    for (let value of specialCharsList) {
        let char = $("<span/>").addClass(specialCharClass).appendTo(parentElement);
        char.text(value);
    }
}
function createUsernamePopover(containerSelector,contentSelector){
    $('input[type=text][data-toggle="popover"]').popover({
        html:true,
        container:containerSelector,
        trigger: 'hover',
        content:function(){
            return $(contentSelector).html();
        }
    });
}

function createPasswordPopover(containerSelector,contentSelector){
    $('input[type=password][data-toggle="popover"]').popover({
        html:true,
        container:containerSelector,
        trigger: 'hover',
        content:function(){
            return $(contentSelector).html();
        }
    });

}
function getSpecialCharacters(){
    return specialCharsList;
}

function isLowerLetter(key){
    return key >= 'a' && key <= 'z';
}
function isUpperLetter(key){
    return key >= 'A' && key <= 'Z';
}
function isDigit(key){
    return key >= '0' && key <= '9';
}
function specialChars(key) {
    return !!specialCharsList.includes(key);
}
function validUsernameChar(key) {
    return isDigit(key)
        ||isLowerLetter(key)
        || isUpperLetter(key);
}

function initializeUsernameErrorList (errorsList) {
    errorsList.children(".username-error").each((k, li) => {
        li.remove();
    });
}
function initializePasswordErrorList(errorsList) {
    errorsList.children(".password-error").each((k, li) => {
        li.remove();
    });
}
function initializePasswordConfirmErrorList(errorsList) {
    errorsList.children(".passwordConfirm-error").each((k, li) => {
        li.remove();
    });
}
function clearPasswordsInput(passwordInput, passwordConfirmInput,errorsList){
    clearPasswordInput(passwordInput,errorsList);
    clearPasswordConfirmInput(passwordConfirmInput,errorsList)
}
function clearPasswordConfirmInput(passwordConfirmInput,errorsList){
    passwordConfirmInput.val('');
    validationErrors.passwordConfirm = {};
    initializePasswordConfirmErrorList(errorsList);
}
function clearPasswordInput(passwordInput,errorsList){
    passwordInput.val('');
    validationErrors.password = {};
    initializePasswordErrorList(errorsList);
}
function clearUsernameInput(usernameInput, errorsList){
    usernameInput.val('');
    validationErrors.username = {};
    initializeUsernameErrorList(errorsList);
}
//validate the username, check if it contains only valid chars
// which is defined in validaUsernameChar function.
function _validateUsername(username){
    let hasInvalidChar = false;
    let isValidUsername = true;
    validationErrors.username = {};
    for (let i = 0; i < username.length; i++) {
        if (!validUsernameChar(username[i])) {
            hasInvalidChar = true;
            break;
        }
    }
    let usernameLength = username.length;

    if (hasInvalidChar) {
        validationErrors.username.invalidChar = 'Username can only contains digits or letters';
        isValidUsername = false;
    }
    if (usernameLength > maxUsernameLength) {
        validationErrors.username.maxLength = 'Username is too long.';
        isValidUsername = false;
    }
    return isValidUsername;
}

function validateUsername(usernameInput, showErrors,errorsList) {
    let username = usernameInput.val();
    _validateUsername(username);
    if(username.length === 0){
        clearUsernameInput(usernameInput,errorsList);
    }
    if(showErrors) {
        showErrors(errorsList);
    }
}

function isValidUsername(usernameInput){
    return _validateUsername(usernameInput.val());
}
function validateLoginForm(usernameInput, passwordInput){
    return _validateUsername(usernameInput.val())
    && _validatePassword(passwordInput.val());
}
function _validatePassword(password){
    let hasInvalidChar = false;
    validationErrors.password = {};
    let hasUpper = false;
    let hasLower = false;
    let hasDigit = false;
    let hasSpecial = false;
    let isValidPassword = true;
    for (let i = 0; i < password.length; i++) {
        let key = password[i];
        if(isDigit(key)){
            hasDigit = true;
        }
        else if(isLowerLetter(key)){
            hasLower = true;
        }
        else if(isUpperLetter(key)){
            hasUpper = true;
        }
        else if (specialChars(key)) {
            hasSpecial = true;
        }
        else{
            hasInvalidChar = true;
        }
    }
    passwordLength = password.length;

    if (hasInvalidChar) {
        validationErrors.password.invalidChar = "Password contains invalid characters";
        isValidPassword= false;
    }
    if (passwordLength > maxPasswordLength) {
        validationErrors.password.maxLength = 'Password is too long.';
        isValidPassword= false;
    }
    if (passwordLength < minPasswordLength && passwordLength > 0) {
        validationErrors.password.minLength = "Password is too short.";
        isValidPassword= false;
    }
    if(!hasLower){
        validationErrors.password.lowerCase = 'Password must contain 1 or more lowercase characters.';
        isValidPassword= false;
    }
    if(!hasUpper){
        validationErrors.password.upperCase = 'Password must contain 1 or more uppercase characters.';
        isValidPassword= false;
    }
    if(!hasDigit){
        validationErrors.password.digit = 'Password must contain 1 or more digits';
        isValidPassword= false;
    }
    if(!hasSpecial){
        validationErrors.password.specialChar = 'Password must contain 1 or more special character';
        isValidPassword= false;
    }

    return isValidPassword;
}
function validatePassword(passwordInput,passwordConfirmInput, showErrors, errorsList, passwordStrengthBar) {
    let password = passwordInput.val();
    passwordStrengthBar.show();
    clearPasswordConfirmInput(passwordConfirmInput,errorsList);
    _validatePassword(password);
    if(password.length === 0){
        clearPasswordInput(passwordInput,errorsList);
        passwordStrengthBar.hide();
    }
    showErrors(errorsList);
}

//starts confirm password validation when key press
function validPasswordConfirm(passwordInput,passwordConfirmInput,showErrors,errorsList) {
    let confirmValue = passwordConfirmInput.val();
    validationErrors.passwordConfirm = {};
    let valid =  confirmValue === passwordInput.val();

    if(!valid){
        validationErrors.passwordConfirm.notMatch = "Password fields not match.";
    }
    return showErrors(errorsList);
}

function showUsernameErrors(errorsList){
    let hasError = false;
    initializeUsernameErrorList(errorsList);
    for (let error in validationErrors.username) {
        let li = $('<li/>').appendTo(errorsList).addClass('username-error');
        let small = $('<small/>').appendTo(li);
        small.text(validationErrors.username[error]);
        hasError = true;
    }
    return hasError;
}

function showPasswordErrors(errorsList){
    let hasError = false;
    initializePasswordErrorList(errorsList);
    initializePasswordConfirmErrorList(errorsList);

    for (let error in validationErrors.password) {
        let li = $('<li/>').appendTo(errorsList).addClass('password-error');
        let small = $('<small/>').appendTo(li);
        small.text(validationErrors.password[error]);
        hasError = true;
    }
    for (let error in validationErrors.passwordConfirm) {
        let li = $('<li/>').appendTo(errorsList).addClass('passwordConfirm-error');
        let small = $('<small/>').appendTo(li);
        small.text(validationErrors.passwordConfirm[error]);
        hasError = true;
    }
    return hasError;
}
function passwordStrength(){
    let options = {
        common: {minChar: 6},
        ui: {
            showVerdictsInsideProgressBar: true,
        }
    };
    $('#password').pwstrength(options);
    $('div.progress').addClass("fadeIn fadeIn-soon");
    let passwordStrengthBar = $("div.progress");
    passwordStrengthBar.hide();
    return passwordStrengthBar;
}
