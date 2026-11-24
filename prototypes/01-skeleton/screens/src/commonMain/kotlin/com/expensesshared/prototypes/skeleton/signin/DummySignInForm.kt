package com.expensesshared.prototypes.skeleton.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.password
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.expensesshared.prototypes.skeleton.theme.PrototypeSpacing

/**
 * US-2 dummy sign-in form (ADR-0001 accessibility study). Android and web (Q10 pending).
 * All labels are PROPOSALS pending senior answer Q7 / Q10.
 * Submitting does nothing but toggle a local message: no network, no storage, no log.
 */
data class DummySignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: Boolean = false,
    val submitted: Boolean = false,
)

enum class SignInField { Email, Password, Submit }

object SignInTexts {
    const val BANNER = "Formulaire factice — ne saisissez aucun identifiant réel"
    const val TITLE = "Connexion (factice)"
    const val EMAIL_LABEL = "Adresse e-mail"
    const val PASSWORD_LABEL = "Mot de passe"
    const val SUBMIT = "Se connecter"

    // Generic: only says the field is empty. No format rule invented, nothing about accounts.
    const val EMAIL_EMPTY_ERROR = "Saisissez une adresse e-mail."
    const val SUBMITTED = "Formulaire factice : rien n'est envoyé."
}

/** Pure transition used by the stateful wrapper. Only rule: empty e-mail => generic field error. */
fun DummySignInUiState.submit(): DummySignInUiState =
    if (email.isBlank()) copy(emailError = true, submitted = false) else copy(emailError = false, submitted = true)

@Composable
fun DummySignInRoute(
    initial: DummySignInUiState = DummySignInUiState(),
    initialFocus: SignInField? = null,
    modifier: Modifier = Modifier,
) {
    var state by remember { mutableStateOf(initial) }
    DummySignInForm(
        state = state,
        onEmailChange = { state = state.copy(email = it, emailError = false, submitted = false) },
        onPasswordChange = { state = state.copy(password = it, submitted = false) },
        onSubmit = { state = state.submit() },
        initialFocus = initialFocus,
        modifier = modifier,
    )
}

@Composable
fun DummySignInForm(
    state: DummySignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    initialFocus: SignInField? = null,
) {
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val submitFocus = remember { FocusRequester() }
    LaunchedEffect(initialFocus) {
        when (initialFocus) {
            SignInField.Email -> emailFocus.requestFocus()
            SignInField.Password -> passwordFocus.requestFocus()
            SignInField.Submit -> submitFocus.requestFocus()
            null -> Unit
        }
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PrototypeSpacing.screenPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier.widthIn(max = PrototypeSpacing.contentMaxWidth).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(PrototypeSpacing.stackGap),
            ) {
                // Anti-phishing banner (STRIDE, web/Spoofing).
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = SignInTexts.BANNER,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(12.dp),
                    )
                }
                Text(
                    text = SignInTexts.TITLE,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.semantics { heading() },
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    label = { Text(SignInTexts.EMAIL_LABEL) },
                    singleLine = true,
                    isError = state.emailError,
                    supportingText = if (state.emailError) {
                        { Text(SignInTexts.EMAIL_EMPTY_ERROR) }
                    } else {
                        null
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = PrototypeSpacing.minTarget)
                        .focusRequester(emailFocus)
                        .semantics {
                            contentType = ContentType.EmailAddress
                            if (state.emailError) error(SignInTexts.EMAIL_EMPTY_ERROR)
                        },
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = onPasswordChange,
                    label = { Text(SignInTexts.PASSWORD_LABEL) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = PrototypeSpacing.minTarget)
                        .focusRequester(passwordFocus)
                        .semantics {
                            password()
                            contentType = ContentType.Password
                        },
                )

                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = PrototypeSpacing.minTarget)
                        .focusRequester(submitFocus),
                ) { Text(SignInTexts.SUBMIT) }

                if (state.submitted) {
                    Text(
                        text = SignInTexts.SUBMITTED,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    )
                }
            }
        }
    }
}
