var ReactNative = require('react-native')
var Contacts = ReactNative.NativeModules.Contacts;

export default class RCTContacts {
  static getAll(callback, sortBy = 'lastName') {
    if (sortBy !== 'lastName' && sortBy !== 'firstName') {
      sortBy = 'lastName';
    }
    Contacts.getAll(sortBy, callback);
  }
}
