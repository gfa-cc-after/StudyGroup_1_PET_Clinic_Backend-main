
<h1>Project phase - Study Group 1 - Backend_main</h1>
<h1>Setup application mail password</h1>

<h2>Step 1. Creating App Passwords in Google Account</h2>

<h3>1. Sign in to your Google Account:</h3>
<p>Open <a href="https://myaccount.google.com/" target="_blank">Google Account settings</a> and sign in.</p>

<h3>2. Open Security Settings:</h3>
    <p>In the left-hand menu, select "Security."</p>

<h3>3. Enable Two-Step Verification:</h3>
    <p>The "App passwords" option is only available if two-step verification is enabled. If you haven't enabled it yet, follow the instructions to turn on two-step verification.</p>

<h3>4. Manage App Passwords:</h3>
    <p>Under the "Signing in to Google" section, click on "App passwords." You may need to enter your password again for security reasons.</p>

<h3>5. Select App and Device:</h3>
    <p>On the "App passwords" page, select the app and device for which you need the password. For example, you can choose options like "Mail" and "Windows Computer," then click "Generate."</p>

<h3>6. Copy and Use the Password:</h3>
    <p>A generated app password will appear in a yellow box. Copy this password and use it in the app or device to access your Google account.</p>

<h3>7. Store the Password Securely:</h3>
    <p>The app password is only shown once. Ensure you store it securely or use it immediately, as you won't be able to view it again later.</p>

<h3>Important Notes:</h3>
    <ul>
        <li>Use app passwords only if an app or device doesn't support two-step verification.</li>
        <li>If you no longer need an app password, you can delete it in your Google Account settings.</li>
    </ul>

<p>By following these steps, you can successfully create and use app passwords in your Google account.</p>

<h2>Step 2. Put the password in the code:</h2>

<h3>Create new ".env" file under the root of the repo.</h3>
<p>Note: Use the syntax of the .env.sample for the .env file. Don't delete the sample file, to have a backup sample of the syntax. </p>

<h3>This way we won't commit the .env and this way the secret is safe. We can use the .env for further secrets.</h3>
