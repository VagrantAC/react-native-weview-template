import React from 'react';
import {WebView} from 'react-native-webview';
import {StyleSheet} from 'react-native';

function App(): JSX.Element {
  return (
    <WebView
      source={{uri: 'http://localhost:7890/index.html'}}
      style={styles.webview}
    />
  );
}

const styles = StyleSheet.create({
  webview: {
    flex: 1,
  },
});

export default App;
