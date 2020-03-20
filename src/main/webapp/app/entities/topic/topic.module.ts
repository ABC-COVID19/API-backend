import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Abccovid19SharedModule } from 'app/shared/shared.module';
import { TopicComponent } from './topic.component';
import { TopicDetailComponent } from './topic-detail.component';
import { TopicUpdateComponent } from './topic-update.component';
import { TopicDeleteDialogComponent } from './topic-delete-dialog.component';
import { topicRoute } from './topic.route';

@NgModule({
  imports: [Abccovid19SharedModule, RouterModule.forChild(topicRoute)],
  declarations: [TopicComponent, TopicDetailComponent, TopicUpdateComponent, TopicDeleteDialogComponent],
  entryComponents: [TopicDeleteDialogComponent]
})
export class Abccovid19TopicModule {}
