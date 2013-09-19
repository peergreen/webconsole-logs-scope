/**
 * Peergreen S.A.S. All rights reserved.
 * Proprietary and confidential.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.webconsole.scope.logs;

import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.Scope;
import com.peergreen.webconsole.vaadin.tabs.TabScope;

/**
 * @author Florent Benoit
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.scope")
@Scope(value = "logs", iconClass = "icon-chat")
public class LogsScope extends TabScope {

    public LogsScope() {
        super("Logs", false);
    }
}
