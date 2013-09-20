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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;

import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

import com.peergreen.kernel.system.LogListener;
import com.peergreen.kernel.system.StreamLine;
import com.peergreen.kernel.system.SystemStream;
import com.peergreen.webconsole.Extension;
import com.peergreen.webconsole.ExtensionPoint;
import com.peergreen.webconsole.Inject;
import com.peergreen.webconsole.Scope;
import com.peergreen.webconsole.UIContext;
import com.peergreen.webconsole.scope.logs.stream.TableEntry;
import com.peergreen.webconsole.scope.logs.stream.TextColumnGenerator;
import com.peergreen.webconsole.scope.logs.stream.TypeFilter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemDescriptionGenerator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.VerticalLayout;

/**
 * @author Florent Benoit
 */
@Extension
@ExtensionPoint("com.peergreen.webconsole.scope")
@Scope(value = "logs", iconClass = "icon-chat")
@Provides(specifications=SystemStream.class, properties={@StaticServiceProperty(name="stream.type", value="{System.out,System.err}", type="java.lang.String[]")})
public class LogsScope extends VerticalLayout implements SystemStream, LogListener {

    @Inject
    private UIContext uiContext;

    private final Table table;

    private final BeanItemContainer<TableEntry> container;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SS");

    private TypeFilter filter;

    public LogsScope() {
        setMargin(true);
        setSpacing(true);
        setSizeFull();

        FormLayout form = new FormLayout();
        form.setWidth("100%");
        form.setSpacing(true);
        form.setMargin(true);
        HorizontalLayout systemLayout = new HorizontalLayout();
        systemLayout.setCaption("JVM System streams:");
        form.addComponent(systemLayout);
        HorizontalLayout loggerLayout = new HorizontalLayout();
        loggerLayout.setCaption("Loggers:");
        form.addComponent(loggerLayout);
        addComponent(form);

        final CheckBox systemOut = new CheckBox("out");
        systemOut.setValue(true);
        systemLayout.addComponent(systemOut);

        this.container = new BeanItemContainer<TableEntry>(TableEntry.class);
        filter = new TypeFilter();
        container.addContainerFilter(filter);

        systemOut.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                container.removeAllContainerFilters();
                filter.setAcceptSystemOut(value);
                container.addContainerFilter(filter);
            }
        });


        CheckBox systemErr = new CheckBox("err");
        systemLayout.addComponent(systemErr);
        systemErr.setValue(true);
        systemErr.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                container.removeAllContainerFilters();
                filter.setAcceptSystemErr(value);
                container.addContainerFilter(filter);
            }
        });

        CheckBox loggerInfo = new CheckBox("Info");
        loggerLayout.addComponent(loggerInfo);
        loggerInfo.setValue(true);
        loggerInfo.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                container.removeAllContainerFilters();
                filter.setAcceptLoggerInfo(value);
                container.addContainerFilter(filter);
            }
        });


        CheckBox loggerWarning = new CheckBox("Warning");
        loggerLayout.addComponent(loggerWarning);
        loggerWarning.setValue(true);
        loggerWarning.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                container.removeAllContainerFilters();
                filter.setAcceptLoggerWarning(value);
                container.addContainerFilter(filter);
            }
        });


        CheckBox loggerError = new CheckBox("Error");
        loggerLayout.addComponent(loggerError);
        loggerError.setValue(true);
        loggerError.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                boolean value = (Boolean) event.getProperty().getValue();
                container.removeAllContainerFilters();
                filter.setAcceptLoggerError(value);
                container.addContainerFilter(filter);
            }
        });

        Button clearButton = new Button("clear");
        //horizontalLayout.addComponent(clearButton);
        clearButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                container.removeAllItems();

            }
        });


        this.table = new Table();
        table.setSizeFull();
        table.setImmediate(true);

        // Define the names and data types of columns.
        table.addContainerProperty("date", Date.class,  null);
        table.addContainerProperty("type", String.class, null);
        table.addContainerProperty("caller", String.class, null);
        table.addContainerProperty("text", String.class,  "empty");

        table.setContainerDataSource(container);
        table.addGeneratedColumn("text", new TextColumnGenerator());

        table.setItemDescriptionGenerator(new ItemDescriptionGenerator() {
            @Override
            public String generateDescription(Component source, Object itemId, Object propertyId) {
                TableEntry tableEntry = (TableEntry) itemId;

                return simpleDateFormat.format(tableEntry.getDate()).concat(" : ").concat(tableEntry.getType()).concat(" : ").concat(tableEntry.getCaller());

            }
        });

        table.setVisibleColumns(new Object[] {"text"});
        table.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
        table.setSortEnabled(true);
        addComponent(table);
        setExpandRatio(table, 1.5f);
    }

    @Override
    public void newLine(StreamLine streamLine) {
        String caller = streamLine.getSourceClassName().concat(".").concat(streamLine.getSourceMethodName()).concat(":").concat(String.valueOf(streamLine.getSourceLineNumber()));

        final TableEntry tableEntry = new TableEntry();
        tableEntry.setDate(new Date(streamLine.getTimestamp()));
        tableEntry.setCaller(caller);
        tableEntry.setText(streamLine.getText());
        tableEntry.setType(streamLine.getType().name());

        container.addItem(tableEntry);
        table.sort(new Object[] {"date"}, new boolean[] {true});
    }

    @Override
    public void log(LogRecord logRecord) {

        String caller = logRecord.getSourceClassName().concat(".").concat(logRecord.getSourceMethodName());

        final TableEntry tableEntry = new TableEntry();
        tableEntry.setDate(new Date(logRecord.getMillis()));
        tableEntry.setCaller(caller);
        tableEntry.setText(logRecord.getMessage());
        tableEntry.setType(logRecord.getLevel().getName());

        container.addItem(tableEntry);
        table.sort(new Object[] {"date"}, new boolean[] {true});
    }
}
